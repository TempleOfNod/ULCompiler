package IR;
import java.util.ArrayList;
import Types.*;

// Prints Jasmin assembly from AST
// TODO: make a version that reads IR and writes Jasmin

public class JasminPrintVisitor {
    String progName;
    int extraLabelCount;

    /*
        Program & Function
    */
    public void visit(IRProgram o) {
        progName = o.name;

        System.out.println(".class public " + progName);
        System.out.println(".super java/lang/Object");

        // print functions
        for (IRFunction f : o.functions) {
            f.accept(this);
        }

        // wrapper main method that calls real main
        System.out.println("\n;Jason's Jasmin Boilerplate");
        System.out.println(".method public static main([Ljava/lang/String;)V");
        System.out.println("\t.limit locals 1");
        System.out.println("\t.limit stack 4");
        System.out.println("\tinvokestatic " + progName + "/__main()V");
        System.out.println("\treturn");
        System.out.println(".end method\n");

        // initialize
        System.out.println(".method public <init>()V");
        System.out.println("\taload_0");
        System.out.println("\tinvokenonvirtual java/lang/Object/<init>()V");
        System.out.println("\treturn");
        System.out.println(".end method");
    }

    public void visit(IRFunction o) {
        extraLabelCount = 0;

        // function declaration
        System.out.print("\n.method public static ");
        if (o.name.equals("main")) {
            System.out.println("__main()V");
        }
        else {
            System.out.print(o.name);

            // parameters
            System.out.print("(");
            for (Type t : o.paraTypes) {
                System.out.print(t.toJasminString());
            }
            System.out.print(")");

            System.out.println(o.type.toJasminString());
        }

        // variable declarations
        System.out.print("\t.limit locals ");
        System.out.println(Integer.toString(o.temps.size()));
        
        for (int i = 0; i < o.temps.size(); i++) {
            Temp t = o.temps.get(i);

            System.out.print("\t.var " + Integer.toString(i) + " is ");
            if (i < o.varNames.size()) {
                System.out.print(o.varNames.get(i));
            }
            else {
                System.out.print(t.toString());
            }
            System.out.print(" " + t.type.toJasminString());
            System.out.println(" from L_START to L_END");
        }
        System.out.println("\t.limit stack 16");
        System.out.println("\nL_START:");

        // variable initialization
        for (int i = o.paraTypes.size(); i < o.temps.size(); i++) {
            char prefix = o.temps.get(i).type.jasminPrefix();
            if (prefix == 'i') {
                System.out.println("\tldc 0");
            }
            else if (prefix == 'f') {
                System.out.println("\tldc 0.0");
            }
            else {
                System.out.println("\taconst_null");
            }
            System.out.println("\t" + prefix + "store " + Integer.toString(i));
        }
        System.out.println();
        
        // instructions
        for (Instruction in : o.instructions) {
            System.out.println("\n;\t\t" + in.toString());
            in.accept(this);
        }

        // end
        System.out.println("L_END:");
        System.out.println(".end method");
    }

    /*
        Operators
    */
    public void arithmeticOp(BinOperator o, String cmd) {
        // take two numbers and return a new number
        String prefix = "\t" + o.type.jasminPrefix();
        System.out.println(prefix + "load " + o.left.num);
        System.out.println(prefix + "load " + o.right.num);
        System.out.println(prefix + cmd);
    }

    public void comparisonOp(BinOperator o, String cmd) {
        // push 0 or 1 to stack
        String labelA = extraLabel();
        String labelB = extraLabel();
            
        String prefix = "\t" + o.type.jasminPrefix();
        System.out.println(prefix + "load " + o.left.num);
        System.out.println(prefix + "load " + o.right.num);

        if (o.type instanceof FloatType) {
            // float
            System.out.println("\tfcmpg");
            System.out.println("\tif" + cmd + " " + labelA);
        }
        else if (o.type instanceof StringType) {
            // string
            System.out.println("\tinvokevirtual java/lang/String/compareTo(Ljava/lang/String;)I");
            System.out.println("\tif" + cmd + " " + labelA);
        }
        else {
            // int (including boolean and char)
            System.out.println("\tif_icmp" + cmd + " " + labelA);
        }
        System.out.println("\tldc 0");
        System.out.println("\tgoto " + labelB);
        System.out.println("\t" + labelA + ":");
        System.out.println("\tldc 1");
        System.out.println("\t" + labelB + ":");
    }

    public void visit(AddOperator o) {
        if (!(o.type instanceof StringType)) {
            // add numbers
            arithmeticOp(o, "add");
        }
        else {
            // append strings
            System.out.println("\tnew java/lang/StringBuffer");
	        System.out.println("\tdup");
	        System.out.println("\tinvokenonvirtual java/lang/StringBuffer/<init>()V");
	        System.out.println("\taload " + Integer.toString(o.left.num));
	        System.out.println("\tinvokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;");
	        System.out.println("\taload " + Integer.toString(o.right.num));
	        System.out.println("\tinvokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;");
	        System.out.println("\tinvokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;");
        }
    }

    public void visit(SubtractOperator o) {
        arithmeticOp(o, "sub");
    }

    public void visit(MultOperator o) {
        arithmeticOp(o, "mul");
    }

    public void visit(EqualOperator o) {
        comparisonOp(o, "eq");
    }

    public void visit(LessThanOperator o) {
        comparisonOp(o, "lt");
    }

    /*
        Single Instructions
    */
    public void visit(AssignCall o) {
        // reuse CallInstruction visit for the calling part
        /* note:    AssignCall is subclass of CallInstruction
                    so this is an upcast and it should be safe
                    even though it breaks the visitor pattern
        */
        visit((CallInstruction)o);
        System.out.print("\t" + o.left.type.jasminPrefix());
        System.out.println("store " + o.left.num);
    }
    
    public void visit(AssignConstant o) {
        // load constant
        System.out.print("\tldc ");
        if (o.right instanceof BoolConstant) {
            BoolConstant bc = (BoolConstant)o.right;
            System.out.println(bc.value? "1" : "0");
        }
        else if (o.right instanceof CharConstant) {
            CharConstant cc = (CharConstant)o.right;
            System.out.println(Integer.toString(cc.value));
        }
        else {
            System.out.println(o.right.toString());
        }

        // store to temp
        System.out.print("\t" + o.left.type.jasminPrefix());
        System.out.println("store " + Integer.toString(o.left.num));
    }

    public void visit(AssignFromArray o) {
        // load array element and store to left side
        String prefix = "\t" + o.left.type.jasminPrefix();
        System.out.println("\taload " + Integer.toString(o.array.num));
        System.out.println("\tiload " + Integer.toString(o.index.num));
        System.out.println(prefix + "aload");
        System.out.println(prefix + "store " + Integer.toString(o.left.num));
    }

    public void visit(AssignInversion o) {
        // flip right side and store to left side
        System.out.println("\tiload " + o.right.num);
        System.out.println("\tldc 1");
        System.out.println("\tixor");
        System.out.println("\tistore " + o.left.num);

    }

    public void visit(AssignNewArray o) {
        // load array size to create array
        System.out.println("\tldc " + Integer.toString(o.atype.size));

        System.out.print("\tnewarray ");
        if (o.atype.eleType instanceof StringType) {
            System.out.println("java/lang/String");
        }
        else {
            // atomic types
            System.out.println(o.atype.eleType.toString());
        }

        System.out.println("\tastore " + Integer.toString(o.left.num));
    }

    public void visit(AssignOperation o) {
        // load result of operation and store to left side
        o.right.accept(this);
        System.out.print("\t" + o.left.type.jasminPrefix());
        System.out.println("store " + Integer.toString(o.left.num));
    }

    public void visit(AssignTemp o) {
        // load right side and store to left side
        System.out.print("\t" + o.right.type.jasminPrefix());
        System.out.println("load " + Integer.toString(o.right.num));
        System.out.print("\t" + o.left.type.jasminPrefix());
        System.out.println("store " + Integer.toString(o.right.num));
    }

    public void visit(AssignToArray o) {
        // load right side and store to array element
        String prefix = "\t" + o.right.type.jasminPrefix();
        System.out.println("\taload " + Integer.toString(o.array.num));
        System.out.println("\tiload " + Integer.toString(o.index.num));
        System.out.println(prefix + "load " + Integer.toString(o.right.num));
        System.out.println(prefix + "astore");
    }

    public void visit(CallInstruction o) {
        String argTypes = "";
        // load parameters
        for (Temp t : o.arguments) {
            System.out.print("\t" + t.type.jasminPrefix());
            System.out.println("load " + t.num);
            argTypes += t.type.toJasminString();
        }

        // invoke method
        System.out.print("\tinvokestatic " + progName + "/" + o.name + "(");
        System.out.println(argTypes + ")" + o.type.toJasminString());
    }

    public void visit(JumpInstruction o) {
        System.out.println("\tgoto L" + Integer.toString(o.target));
    }

    public void visit(IfJumpInstruction o) {
        // jump if loaded boolean is true
        System.out.println("\tiload " + Integer.toString(o.condition.num));
        System.out.println("\tifne L" + Integer.toString(o.target));
    }

    public void visit(LabelInstruction o) {
        System.out.println("L" + Integer.toString(o.num) + ":");
    }

    public void visit(PrintInstruction o) {
        // call java print
        System.out.println("\tgetstatic java/lang/System/out Ljava/io/PrintStream;");
        System.out.print("\t" + o.temp.type.jasminPrefix());
        System.out.println("load " + Integer.toString(o.temp.num));
        System.out.print("\tinvokevirtual java/io/PrintStream/print(");
        System.out.println(o.temp.type.toJasminString() + ")V");
    }

    public void visit(PrintlnInstruction o) {
        // call java println
        System.out.println("\tgetstatic java/lang/System/out Ljava/io/PrintStream;");
        System.out.print("\t" + o.temp.type.jasminPrefix());
        System.out.println("load " + Integer.toString(o.temp.num));
        System.out.print("\tinvokevirtual java/io/PrintStream/println(");
        System.out.println(o.temp.type.toJasminString() + ")V");
    }

    public void visit(ReturnInstruction o) {
        // return a temp if needed to
        if (o.temp != null) {
            String prefix = "\t" + o.temp.type.jasminPrefix();
            System.out.print(prefix + "load ");
            System.out.println(Integer.toString(o.temp.num));
            System.out.println(prefix + "return");
        }
        else {
            System.out.println("\treturn");
        }
    }

    String extraLabel() {
        return "L_" + Integer.toString(extraLabelCount++);
    }
}