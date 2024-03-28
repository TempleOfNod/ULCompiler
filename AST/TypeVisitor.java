package AST;
import java.util.ArrayList;
import Types.*;

public class TypeVisitor extends Visitor {
    // environment
    ArrayList<FunctionSymbol> funcEnv;
    ArrayList<Symbol> varEnv;
    FunctionDeclaration main;

    // literal types
    BooleanType booleanType;
    CharType charType;
    FloatType floatType;
    IntType intType;
    StringType stringType;

    // inherited attributes
    int funcIndex;
    boolean funcCall;

    public TypeVisitor() {
        funcEnv = null;
        varEnv = null;
        main = null;

        booleanType = new BooleanType();
        charType = new CharType();
        floatType = new FloatType();
        intType = new IntType();
        stringType = new StringType();

        funcIndex = -1;
        funcCall = false;
    }

    public Object visit(AddExpression o) throws Exception {
        Type type = null;
        try {
            // get type of first expression
            type = (Type)(o.exprs.get(0).accept(this));
            Boolean isStr = type instanceof StringType;

            // check if type is allowed in add expression
            if (type instanceof VoidType 
                    || type instanceof BooleanType
                    || type instanceof ArrayType) {
                throw new SemanticException(
                        o.getLine(),
                        o.getPosition(),
                        "cannot add " + type.toString());
            }

            for (int i = 1; i < o.exprs.size(); i++) {
                // check if type is allowed in subtract expression
                if (isStr && !o.sign.get(i)) {
                    throw new SemanticException(o,
                            "cannot subtract string");
                }

                // check if expressions have same type
                Expression other = o.exprs.get(i);
                Type otype = (Type)(other.accept(this));
                if (!type.equals(otype)) {
                    throw new SemanticException(o,
                            "inconsistent types in add expression");
                }
            }
        }
        catch (Exception e) {
            throw e;
        }
        return type;
    }

    public Object visit(ArrayAssignStatement o) throws Exception {
        Type l = null;
        Type r = null;
        try {
            // check if types are consistent
            l = (Type)(o.ref.accept(this));
            r = (Type)(o.value.accept(this));
        }
        catch (Exception e) {
            throw e;
        }

        if (!l.equals(r)) {
            throw new SemanticException(o,
                    "array element type unmatch");
        }
        
        return null;
    }

    public Object visit(ArrayReference o) throws Exception {
        ArrayType type = null;
        try {
            // check if index is int
            if (!(o.in.accept(this) instanceof IntType)) {
                throw new SemanticException(o,
                        "array index is not integer");
            }
            
            // return variable type
            type = (ArrayType)(o.id.accept(this));
        }
        catch (Exception e) {
            throw e;
        }
        return type.eleType;
    }

    public Object visit(AssignStatement o) throws Exception {
        try {
            Type l = (Type)(o.id.accept(this));
            Type r = (Type)(o.value.accept(this));

            // check if types are consistent
            if (!l.equals(r)) {
                throw new SemanticException(o,
                        "expression type must be " + l.toString());
            }
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Object visit(Block o) throws Exception {
        try {
            o.sl.accept(this);
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Object visit(BoolLiteral o) throws Exception {
        return booleanType;
    }

    public Object visit(CharLiteral o) throws Exception {
        return charType;
    }

    public Object visit(EqualExpression o) throws Exception {
        try {
            Type l = (Type)(o.left.accept(this));
            Type r = (Type)(o.right.accept(this));

            // check if type is allowed
            if (l instanceof VoidType || l instanceof ArrayType) {
                throw new SemanticException(o,
                        "invalid type " + l.toString());
            }

            // check if type is consistent
            if (!l.equals(r)) {
                throw new SemanticException(o,
                        "compared types are " + l.toString() +
                        " and " + r.toString());
            }
        }
        catch (Exception e) {
            throw e;
        }
        return booleanType;
    }

    public Object visit(ExpressionList o) throws Exception {
        ArrayList<Type> types = new ArrayList<Type>();
        try {
            // get expression types
            if (!o.exprs.isEmpty()) {
                for (Expression e : o.exprs) {
                    types.add((Type)(e.accept(this)));
                }
            }
        }
        catch (Exception e) {
            throw e;
        }
        return types;
    }

    public Object visit(ExpressionStatement o) throws Exception {
        try {
            o.expr.accept(this);
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Object visit(FloatLiteral o) throws Exception {
        return floatType;
    }

    public Object visit(FormalParameter o) throws Exception {
        // return type and id i.e., a symbol
        if (o.type instanceof VoidType) {
            throw new SemanticException(o,
                    "parameter type cannot be void");
        }
        return new Symbol(o.type, o.id);
    }

    public Object visit(FormalParameterList o) throws Exception {
        if (o.para.isEmpty()) {
            return null;
        }

        // return a symbol table to be used in func body and func call
        ArrayList<Symbol> symbols = new ArrayList<Symbol> (o.para.size());
        try {
            for (FormalParameter p : o.para) {
                Symbol ps = (Symbol)(p.accept(this));

                // check if parameters have same name
                for (Symbol s : symbols) {
                    if (s.id.toString().equals(ps.id.toString())) {
                        throw new SemanticException(o,
                                "parameter name already used: " + ps.id.toString());
                    }
                }
                symbols.add(ps);
            }
        }
        catch (Exception e) {
            throw e;
        }
        return symbols;
    }

    public Object visit(Function o) throws Exception {
        // functions are processed in program visit
        return null;
    }

    public Object visit(FunctionBody o) throws Exception {
        try {
            // build varEnv and add parameters (if any)
            ArrayList<Symbol> para = funcEnv.get(funcIndex).paraSymbols;

            if (para != null) {
                varEnv = (ArrayList<Symbol>)para.clone();
            }
            else {
                varEnv = new ArrayList<Symbol> ();
            }

            // visit components
            o.vars.accept(this);
            o.stats.accept(this);
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Object visit(FunctionCall o) throws Exception {
        Type retType = null;
        try {
            // get argument types
            ArrayList<Type> argType = (ArrayList<Type>)(o.args.accept(this));

            // get function type
            funcCall = true;
            FunctionSymbol f = (FunctionSymbol)(o.id.accept(this));
            funcCall = false;

            // check if parameter types match
            if (f.paraSymbols == null) {
                if (!argType.isEmpty()) {
                    throw new SemanticException(o,
                            "function call takes no argument");
                }
            }
            else {
                if ((f.paraSymbols == null && !argType.isEmpty()) ||
                        f.paraSymbols.size() != argType.size()) {
                    throw new SemanticException(o,
                            "wrong number of arguments");
                }
                else {
                    for (int i = 0; i < argType.size(); i++) {
                        if (!f.paraSymbols.get(i).type.equals(
                                argType.get(i))) {
                            throw new SemanticException(o,
                                    "argument " + Integer.toString(i + 1) +
                                    " has wrong type");
                        }
                    }
                }
            }
            // assign return type
            retType = f.type;
        }
        catch (Exception e) {
            throw e;
        } finally {
            funcCall = false;
        }
        return retType;
    }

    public Object visit(FunctionDeclaration o) throws Exception {
        try {
            // check if function name is already used
            if (!funcEnv.isEmpty()) {
                for (FunctionSymbol s : funcEnv) {
                    if (s.id.toString().equals(o.id.toString())) {
                        throw new SemanticException(o,
                                "function name already used: " + o.id.toString());
                    }
                }
            }

            // get parameter symbols
            ArrayList<Symbol> para = (ArrayList<Symbol>)(o.pl.accept(this));

            // add to env
            funcEnv.add(new FunctionSymbol(o.retType, o.id, para));

            // check if this is main function
            if (o.id.toString().equals("main")) {
                main = o;
            }
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Object visit(Identifier o) throws Exception {
        if (!funcCall) {
            // find variable and return the type
            for (Symbol e : varEnv) {
                if (o.toString().equals(e.id.toString())) {
                    return e.type;
                }
            }
            throw new SemanticException(o,
                    "variable " + o.id.toString() + " not found");
        }
        else {
            // find function and return input and output types
            // (return the symbol for convenience)
            for (FunctionSymbol e : funcEnv) {
                if (o.toString().equals(e.id.toString())) {
                    return e;
                }
            }
            throw new SemanticException(o,
                    "function " + o.id.toString() + " not found");
        }
    }

    public Object visit(IfStatement o) throws Exception {
        // check condition type
        if (!((Type)(o.cond.accept(this)) instanceof BooleanType)) {
            throw new SemanticException(o,
                    "condition is not boolean");
        }

        // visit blocks
        if (o.block != null) {
            try {
                o.block.accept(this);
            }
            catch (Exception e) {
                throw e;
            }
        }

        if (o.elseBlock != null) {
            try {
                o.elseBlock.accept(this);
            }
            catch (Exception e) {
                throw e;
            }
        }

        return null;
    }

    public Object visit(IntLiteral o) throws Exception {
        return intType;
    }

    public Object visit(LessThanExpression o) throws Exception {
        try {
            Type l = (Type)(o.left.accept(this));
            Type r = (Type)(o.right.accept(this));

            // check if type is allowed
            if (l instanceof VoidType || l instanceof ArrayType) {
                throw new SemanticException(o,
                        "invalid type " + l.toString());
            }

            // check if type is consistent
            if (!l.equals(r)) {
                throw new SemanticException(o,
                        "compared types are " + l.toString() +
                        " and " + r.toString());
            }
        }
        catch (Exception e) {
            throw e;
        }
        return booleanType;
    }

    public Object visit(MultExpression o) throws Exception {
        Type type = null;
        try {
            // get type of first expression
            type = (Type)(o.exprs.get(0).accept(this));

            // check if type is allowed in mult expression
            if (!(type instanceof IntType || type instanceof FloatType)) {
                throw new SemanticException(o,
                        "cannot multiply " + type.toString());
            }

            // check if expressions have same type
            for (Expression other : o.exprs) {
                Type otype = (Type)(other.accept(this));
                if (!type.equals(otype)) {
                    throw new SemanticException(other,
                            "inconsistent types in multiply expression");
                }
            }
        }
        catch (Exception e) {
            throw e;
        }
        return type;
    }

    public Object visit(ParenExpression o) throws Exception {
        try {
            return o.expr.accept(this);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public Object visit(PrintLnStatement o) throws Exception {
        Type type = null;
        try {
            type = (Type)(o.expr.accept(this));
        }
        catch (Exception e) {
            throw e;
        }

        // check type
        if (type instanceof VoidType || type instanceof ArrayType) {
            throw new SemanticException(o,
                    "cannot print void or array");
        }
        return null;
    }

    public Object visit(PrintStatement o) throws Exception {
        Type type = null;
        try {
            type = (Type)(o.expr.accept(this));
        }
        catch (Exception e) {
            throw e;
        }
        
        // check type
        if (type instanceof VoidType || type instanceof ArrayType) {
            throw new SemanticException(o,
                    "cannot print void or array");
        }
        return null;
    }

    public Object visit(Program o) throws Exception {
        // iterate once to build funcEnv
        funcEnv = new ArrayList<FunctionSymbol> (o.funcs.size());
        try {
            for (Function f : o.funcs) {
                f.decl.accept(this);
            }
        }
        catch (Exception e) {
            throw e;
        }

        // check if main function has right format
        if (main == null) {
            throw new SemanticException(0, 0, "main function not found");
        }
        else if (!(main.retType instanceof VoidType
                && main.pl.para.isEmpty())) {
            throw new SemanticException(main,
                    "main function must be void with no parameters");
        }

        // iterate again to visit function bodies
        try {
            for (funcIndex = 0; funcIndex < o.funcs.size(); funcIndex++) {
                o.funcs.get(funcIndex).body.accept(this);
            }
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Object visit(ReturnStatement o) throws Exception {
        // find function return type
        Type type = funcEnv.get(funcIndex).type;
        Type retType = null;

        if (!(type instanceof VoidType)) {
            if (o.expr != null) {
                // check if expression type matches
                
                try {
                    retType = (Type)(o.expr.accept(this));
                }
                catch (Exception e) {
                    throw e;
                }
                if (!type.equals(retType)) {
                    throw new SemanticException(o,
                            "return type must be " + type.toString());
                }
            }
            else {
                // non-void function with void return
                throw new SemanticException(o,
                        "non-void function must return something");
            }
        }
        else {
            // void function with non-void return
            // Jason said returning another void function is evil or something
            // so that is not allowed either
            if (o.expr != null)
            {
                throw new SemanticException(o,
                        "void function must return nothing");
            }
        }
        return null;
    }

    public Object visit(StatementList o) throws Exception {
        if (o.stats == null) {
            return null;
        }
        
        try {
            for (Statement s : o.stats) {
                // semicolon alone is a null statement
                if (s != null) {
                    s.accept(this);
                }
            }
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Object visit(StrLiteral o) throws Exception {
        return stringType;
    }

    public Object visit(VarDeclaration o) throws Exception {
        // chcek if variable type is void
        if (o.type instanceof VoidType) {
            throw new SemanticException(o,
                    "variable type cannot be void");
        }

        // check if variable name is already used
        if (!varEnv.isEmpty()) {
            for (Symbol s : varEnv) {
                if (s.id.toString().equals(o.id.toString())) {
                    throw new SemanticException(o,
                            "variable name already used: " + o.id.toString());
                }
            }
        }

        // add to env
        varEnv.add(new Symbol(o.type, o.id));
        return null;
    }

    public Object visit(VarDeclarationList o) throws Exception {
        if (o.vars.isEmpty()) {
            return null;
        }
        try {
            for (VarDeclaration v : o.vars) {
                v.accept(this);
            }
        } 
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Object visit(WhileStatement o) throws Exception {
        // check condition type
        if (!((Type)(o.cond.accept(this)) instanceof BooleanType)) {
            throw new SemanticException(o,
                    "condition is not boolean");
        }

        // visit block
        if (o.block != null) {
            try {
                o.block.accept(this);
            }
            catch (Exception e) {
                throw e;
            }
        }
        return null;
    }
}