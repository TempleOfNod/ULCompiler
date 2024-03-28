package IR;
import java.util.ArrayList;
import java.util.TreeMap;
import AST.*;
import Types.*;

/*
    Rules:
        Statement visit method returns null and adds instructions.
        Expression visit method returns a temp and may add instructions.

        Temps.reuse is false for parameter or variable.
        If true, the temp is recycled at the end of a statement or expression.
        Otherwise, it is recycled at the end of a function.

        Assign statement sets assignTo attribute as a certain variable.
        If assignTo is not null, an expression adds result to assign instruction.

        Identifier finds function name if funcCall is true.
        Otherwise, it finds variable name.

        Function call instruction is assigned to assignTo if not null.
        Otherwise it is assigned to a new temp.
        No assignment if a void function is called through expression statement.
*/
public class IRVisitor extends Visitor {
    // environment
    ArrayList<FunctionSymbol> funcEnv;
    TreeMap<Integer, Temp> tempMap;
    IRProgram prog;
    IRFunction func;
    TempFactory factory;
    int labelCount;

    // literal types
    BooleanType booleanType;
    CharType charType;
    FloatType floatType;
    IntType intType;
    StringType stringType;

    // inherited attributes
    Temp assignTo;
    int funcIndex;
    boolean funcCall;
    boolean pureCall;

    public IRVisitor(String progName) {
        funcEnv = null;
        tempMap = null;
        prog = new IRProgram(progName);
        func = null;
        factory = new TempFactory();
        labelCount = 0;

        booleanType = new BooleanType();
        charType = new CharType();
        floatType = new FloatType();
        intType = new IntType();
        stringType = new StringType();

        assignTo = null;
        funcIndex = -1;
        funcCall = false;
        pureCall = false;
    }

    public Object visit(AddExpression o) throws Exception {
        Temp temp;
        Temp state;
        ArrayList<Temp> subs;

        // get subexpressions
        state = assignTo;
        assignTo = null;
        subs = new ArrayList<Temp> (o.exprs.size());
        for (Expression e : o.exprs) {
            subs.add((Temp)(e.accept(this)));
        }
        assignTo = state;

        // create new temp if result is not assigned to existing temp
        if (assignTo == null) {
            temp = factory.getTemp(subs.get(0).type, true);
            put(temp);
        }
        else {
            temp = assignTo;
        }

        // separate expression into a collection of binary instructions
        // t = e1 + e2; t = t - e3; ...
        if (o.sign.get(1)) {
            func.addInstruction(new AssignOperation(temp, new AddOperator(
                    subs.get(0), subs.get(1))));
        }
        else {
            func.addInstruction(new AssignOperation(temp, new SubtractOperator(
                    subs.get(0), subs.get(1))));
        }
        
        for (int i = 2; i < subs.size(); i++) {
            if (o.sign.get(i)) {
                func.addInstruction(new AssignOperation(temp, new AddOperator(
                        temp, subs.get(i))));
            }
            else {
                func.addInstruction(new AssignOperation(temp, new SubtractOperator(
                        temp, subs.get(i))));
            }
        }

        // recycle subexpressions if they can be reused
        for (Temp s : subs) {
            if (s.reuse) {
                factory.returnTemp(s);
            }
        }
        return temp;
        
    }

    public Object visit(ArrayAssignStatement o) throws Exception {
        Temp array;
        Temp index;
        Temp right;
        ArrayType atype;

        // get array variable
        assignTo = null;
        array = (Temp)(o.ref.id.accept(this));
        atype = (ArrayType)array.type;

        // get new temps for index and right side and then assign to them
        // assignments are done in expression visits
        index = factory.getTemp(intType, true);
        put(index);
        assignTo = index;
        o.ref.in.accept(this);

        right = factory.getTemp(atype.eleType, true);
        put(right);
        assignTo = right;
        o.value.accept(this);

        // add array assign instruction
        func.addInstruction(new AssignToArray(array, index, right));

        // clean
        assignTo = null;
        factory.returnTemp(index);
        factory.returnTemp(right);
        return null;
    }

    public Object visit(ArrayReference o) throws Exception {
        // assumes array ref as an expression
        // assigning to an array element is done in the method above
        Temp array;
        Temp index;
        Temp temp;
        Temp state;
        ArrayType atype;

        // get subexpressions
        state = assignTo;
        assignTo = null;
        array = (Temp)(o.id.accept(this));
        index = (Temp)(o.in.accept(this));
        assignTo = state;

        // assign array element to a temp
        atype = (ArrayType)array.type;
        temp = factory.getTemp(atype.eleType, true);
        put(temp);
        func.addInstruction(new AssignFromArray(temp, array, index));

        // recycle index expression
        if (index.reuse) {
            factory.returnTemp(index);
        }

        return temp;
    }

    public Object visit(AssignStatement o) throws Exception {
        Temp left;

        // to avoid duplicate temps, assign instruction is not added here
        // expression visit methods can add it
        // if attribute assignTo is set
        assignTo = null;
        left = (Temp)(o.id.accept(this));

        assignTo = left;
        o.value.accept(this);

        assignTo = null;
        return null;
    }

    public Object visit(Block o) throws Exception {
        o.sl.accept(this);
        return null;
    }

    public Object visit(BoolLiteral o) throws Exception {
        // add assign instruction and return the temp
        Temp temp;
        if (assignTo != null) {
            temp = assignTo;
        }
        else {
            temp = factory.getTemp(booleanType, true);
            put(temp);
        }
        func.addInstruction(new AssignConstant(temp, new BoolConstant(o.value)));
        return temp;
    }

    public Object visit(CharLiteral o) throws Exception {
        // add assign instruction and return the temp
        Temp temp;
        if (assignTo != null) {
            temp = assignTo;
        }
        else {
            temp = factory.getTemp(charType, true);
            put(temp);
        }
        func.addInstruction(new AssignConstant(temp, new CharConstant(o.value)));
        return temp;
    }

    public Object visit(EqualExpression o) throws Exception {
        Temp temp;
        Temp state;
        Temp l;
        Temp r;
        
        // get subexpressions
        state = assignTo;
        assignTo = null;
        l = (Temp)(o.left.accept(this));
        r = (Temp)(o.right.accept(this));
        assignTo = state;

        // create new temp if result is not assigned to existing temp
        if (assignTo == null) {
            temp = factory.getTemp(booleanType, true);
            put(temp);
        }
        else {
            temp = assignTo;
        }

        func.addInstruction(new AssignOperation(temp, new EqualOperator(l, r)));

        // recycle subexpressions if they can be reused
        if (l.reuse) {
            factory.returnTemp(temp);
        }
        if (r.reuse) {
            factory.returnTemp(temp);
        }
        return temp;
    }

    public Object visit(ExpressionList o) throws Exception {
        ArrayList<Temp> temps = new ArrayList<Temp>();
        Temp state;
        
        // get expression temps
        state = assignTo;
        assignTo = null;
        if (!o.exprs.isEmpty()) {
            for (Expression e : o.exprs) {
                temps.add((Temp)(e.accept(this)));
            }
        }
        assignTo = state;
        return temps;
    }

    public Object visit(ExpressionStatement o) throws Exception {
        pureCall = true;
        o.expr.accept(this);
        pureCall = false;
        return null;
    }

    public Object visit(FloatLiteral o) throws Exception {
        // add assign instruction and return the temp
        Temp temp;
        if (assignTo != null) {
            temp = assignTo;
        }
        else {
            temp = factory.getTemp(floatType, true);
            put(temp);
        }
        func.addInstruction(new AssignConstant(temp, new FloatConstant(o.value)));
        return temp;
    }

    public Object visit(FormalParameter o) throws Exception {
        // return type and id i.e., a symbol
        return new Symbol(o.type, o.id);
    }

    public Object visit(FormalParameterList o) throws Exception {
        if (o.para.isEmpty()) {
            return null;
        }

        // return a symbol table to be used in func body and func call
        ArrayList<Symbol> symbols = new ArrayList<Symbol> (o.para.size());
        for (FormalParameter p : o.para) {
            symbols.add((Symbol)(p.accept(this)));
        }
        return symbols;
    }

    public Object visit(Function o) throws Exception {
        // unused
        return null;
    }

    public Object visit(FunctionBody o) throws Exception {
        // build IRFunction and add parameters (if any)
        FunctionSymbol env = funcEnv.get(funcIndex);
        func = new IRFunction(env.id.toString(), env.type);
        tempMap = new TreeMap<Integer, Temp> ();
        
        // add parameter temps to IRFunction
        if (env.paraSymbols != null) {
            for (Symbol s : env.paraSymbols) {
                func.paraTypes.add(s.type);
                func.varNames.add(s.id.toString());
                func.temps.add(factory.getTemp(s.type, false));
            }
        }

        // visit components
        o.vars.accept(this);
        o.stats.accept(this);

        // add return to void function
        if (env.type instanceof VoidType) {
            func.addInstruction(new ReturnInstruction());
        }

        // add intermediate temps in map to IRFunction
        func.temps.addAll(tempMap.values());

        // clean
        labelCount = 0;
        tempMap = null;
        factory.reset();
        return null;
    }

    public Object visit(FunctionCall o) throws Exception {
        Type type;
        Temp temp;
        Temp state;
        ArrayList<Temp> args;

        funcCall = true;
        type = (Type)o.id.accept(this);
        funcCall = false;

        // get subexpressions (arguments)
        state = assignTo;
        assignTo = null;
        args = (ArrayList<Temp>)(o.args.accept(this));
        assignTo = state;

        // three cases:
        // return value is assigned to a variable
        // return value is not assigned
        // return value is assigned to an intermediate temp
        if (assignTo != null) {
            temp = assignTo;
            func.addInstruction(new AssignCall(temp, o.id.toString(), type, args));
        }
        else if (pureCall && type instanceof VoidType) {
            // call is not assigned only under a special case:
            // a void function is called in an expression statement
            temp = null;
            func.addInstruction(new CallInstruction(o.id.toString(), type, args));
        }
        else {
            temp = factory.getTemp(type, true);
            put(temp);
            func.addInstruction(new AssignCall(temp, o.id.toString(), type, args));
        }

        // recycle subexpressions
        if (args != null) {
            for (Temp a : args) {
                if (a.reuse) {
                    factory.returnTemp(a);
                }
            }
        }
        return temp;
    }

    public Object visit(FunctionDeclaration o) throws Exception {
        // build function env
        ArrayList<Symbol> para = (ArrayList<Symbol>)(o.pl.accept(this));
        funcEnv.add(new FunctionSymbol(o.retType, o.id, para));
        return null;
    }

    public Object visit(Identifier o) throws Exception {
        // find variable and return associated temp
        if (!funcCall) {
            for (int i = 0; i < func.varNames.size(); i++) {
                if (o.toString().equals(func.varNames.get(i))) {
                    Temp t = func.temps.get(i);
                    if (assignTo == null) {
                        return t;
                    }
                    else {
                        func.addInstruction(new AssignTemp(assignTo, t));
                        return assignTo;
                    }
                }
            }
        }
        // find function and return its return type
        else {
            for (FunctionSymbol s : funcEnv) {
                if (o.toString().equals(s.id.toString())) {
                    return s.type;
                }
            }
        }
        throw new SemanticException(o,
                o.id.toString() + " not found");
    }

    public Object visit(IfStatement o) throws Exception {
        Temp cond;
        Temp inverse;
        Temp state;
        int label;

        // get condition
        state = assignTo;
        assignTo = null;
        cond = (Temp)(o.cond.accept(this));
        assignTo = state;

        // save labels
        label = labelCount;
        labelCount += 2;

        // add inverse temp and jump instruction
        inverse = factory.getTemp(booleanType, true);
        put(inverse);
        func.addInstruction(new AssignInversion(inverse, cond));
        func.addInstruction(new IfJumpInstruction(inverse, label));

        // add block insructions
        o.block.accept(this);
        
        // end block
        func.addInstruction(new JumpInstruction(label + 1));
        func.addInstruction(new LabelInstruction(label));
        

        // add else block
        if (o.elseBlock != null) {
            o.elseBlock.accept(this);
        }

        // end statement
        func.addInstruction(new LabelInstruction(label + 1));
        
        // recycle temps
        factory.returnTemp(inverse);
        if (cond.reuse) {
            factory.returnTemp(cond);
        }
        return null;
    }

    public Object visit(IntLiteral o) throws Exception {
        // add assign instruction and return the temp
        Temp temp;
        if (assignTo != null) {
            temp = assignTo;
        }
        else {
            temp = factory.getTemp(intType, true);
            put(temp);
        }
        func.addInstruction(new AssignConstant(temp, new IntConstant(o.value)));
        return temp;
    }

    public Object visit(LessThanExpression o) throws Exception {
        Temp temp;
        Temp state;
        Temp l;
        Temp r;

        // get subexpressions
        state = assignTo;
        assignTo = null;
        l = (Temp)(o.left.accept(this));
        r = (Temp)(o.right.accept(this));
        assignTo = state;

        // create new temp if result is not assigned to existing temp
        if (assignTo == null) {
            temp = factory.getTemp(booleanType, true);
            put(temp);
        }
        else {
            temp = assignTo;
        }

        func.addInstruction(new AssignOperation(temp, new LessThanOperator(l, r)));

        // recycle subexpressions if they can be reused
        if (l.reuse) {
            factory.returnTemp(temp);
        }
        if (r.reuse) {
            factory.returnTemp(temp);
        }
        return temp;
    }

    public Object visit(MultExpression o) throws Exception {
        Temp temp;
        Temp state;
        ArrayList<Temp> subs;

        // get subexpressions
        state = assignTo;
        assignTo = null;
        subs = new ArrayList<Temp> (o.exprs.size());
        for (Expression e : o.exprs) {
            subs.add((Temp)(e.accept(this)));
        }
        assignTo = state;

        // create new temp if result is not assigned to existing temp
        if (assignTo == null) {
            temp = factory.getTemp(subs.get(0).type, true);
            put(temp);
        }
        else {
            temp = assignTo;
        }

        // separate expression into a collection of binary instructions
        // t = e1 * e2; t = t * e3; ...
        func.addInstruction(new AssignOperation(temp, new MultOperator(
                subs.get(0), subs.get(1))));    
        
        for (int i = 2; i < subs.size(); i++) {
            func.addInstruction(new AssignOperation(temp, new MultOperator(
                    temp, subs.get(i))));
        }

        // recycle subexpressions if they can be reused
        for (Temp s : subs) {
            if (s.reuse) {
                factory.returnTemp(s);
            }
        }
        return temp;
    }

    public Object visit(ParenExpression o) throws Exception {
        return o.expr.accept(this);
    }

    public Object visit(PrintLnStatement o) throws Exception {
        Temp temp;

        // get expression
        assignTo = null;
        temp = (Temp)(o.expr.accept(this));
        func.addInstruction(new PrintlnInstruction(temp));

        // recycle expression
        if (temp.reuse) {
            factory.returnTemp(temp);
        }
        return null;
    }

    public Object visit(PrintStatement o) throws Exception {
        Temp temp;

        // get expression
        assignTo = null;
        temp = (Temp)(o.expr.accept(this));
        func.addInstruction(new PrintInstruction(temp));

        // recycle expression
        if (temp.reuse) {
            factory.returnTemp(temp);
        }
        return null;
    }

    public Object visit(Program o) throws Exception {
        // iterate once to build funcEnv
        funcEnv = new ArrayList<FunctionSymbol> (o.funcs.size());    
        for (Function f : o.funcs) {
            f.decl.accept(this);
        }

        // iterate again to visit function bodies
        for (funcIndex = 0; funcIndex < o.funcs.size(); funcIndex++) {
            o.funcs.get(funcIndex).body.accept(this);
            prog.addFunction(func);
            func = null;
        }
        return prog;
    }

    public Object visit(ReturnStatement o) throws Exception {
        Temp temp = null;

        // get expression
        if (o.expr != null) {
            assignTo = null;
            temp = (Temp)(o.expr.accept(this));
        }
        func.addInstruction(new ReturnInstruction(temp));

        // recycle expression
        if (temp != null && temp.reuse) {
            factory.returnTemp(temp);
        }
        return null;
    }

    public Object visit(StatementList o) throws Exception {
        if (o.stats == null) {
            return null;
        }
        
        for (Statement s : o.stats) {
            // semicolon alone is a null statement
            if (s != null) {
                s.accept(this);
            }
        }
        return null;
    }

    public Object visit(StrLiteral o) throws Exception {
        // add assign instruction and return the temp
        Temp temp;
        if (assignTo != null) {
            temp = assignTo;
        }
        else {
            temp = factory.getTemp(stringType, true);
            put(temp);
        }
        func.addInstruction(new AssignConstant(temp, new StrConstant(o.value)));
        return temp;
    }

    public Object visit(VarDeclaration o) throws Exception {
        Temp temp;

        // add to IRFunction
        temp = factory.getTemp(o.type, false);
        func.varNames.add(o.id.toString());
        func.temps.add(temp);

        // declare array
        if (o.type instanceof ArrayType) {
            func.addInstruction(new AssignNewArray(temp, (ArrayType)o.type));
        }

        return null;
    }

    public Object visit(VarDeclarationList o) throws Exception {
        // visit each var
        if (o.vars.isEmpty()) {
            return null;
        }
        for (VarDeclaration v : o.vars) {
            v.accept(this);
        }
        return null;
    }

    public Object visit(WhileStatement o) throws Exception {
        Temp cond;
        Temp inverse;
        Temp state;
        int label;

        // add start label
        label = labelCount;
        labelCount += 2;
        func.addInstruction(new LabelInstruction(label));

        // get condition
        state = assignTo;
        assignTo = null;
        cond = (Temp)(o.cond.accept(this));
        assignTo = state;

        // add inverse temp and jump instruction
        inverse = factory.getTemp(booleanType, true);
        put(inverse);
        func.addInstruction(new AssignInversion(inverse, cond));
        func.addInstruction(new IfJumpInstruction(inverse, label + 1));

        // add block insructions
        o.block.accept(this);
        
        // end loop
        func.addInstruction(new JumpInstruction(label));
        func.addInstruction(new LabelInstruction(label + 1));
        
        // recycle temps
        factory.returnTemp(inverse);
        if (cond.reuse) {
            factory.returnTemp(cond);
        }
        return null;
    }

    private void put(Temp temp) {
        // small helper function
        tempMap.put(temp.num, temp);
    }
}