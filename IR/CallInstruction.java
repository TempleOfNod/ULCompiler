package IR;
import java.util.ArrayList;
import Types.Type;
import Types.VoidType;

public class CallInstruction extends Instruction {
    public ArrayList<Temp> arguments;
    public String name;
    public Type type;

    public CallInstruction(String name, Type type, ArrayList<Temp> arguments) {
        this.arguments = arguments;
        this.name = name;
        this.type = type;
    }

    public CallInstruction(String name) {
        arguments = null;
        this.name = name;
        type = new VoidType();
    }

    public String toString() {
        String ret = "\t\tCALL " + name + "(";
        if (arguments != null && !arguments.isEmpty()) {
            ret += arguments.get(0).toString();
            if (arguments.size() > 1) {
                for (int i = 1; i < arguments.size(); i++) {
                    ret += " " + arguments.get(i).toString();
                }
            }
        }
        return ret + ");";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}