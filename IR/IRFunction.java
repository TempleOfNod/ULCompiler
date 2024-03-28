package IR;
import Types.Type;
import java.util.ArrayList;

public class IRFunction {
    public String name;
    public Type type;
    public ArrayList<Type> paraTypes;
    public ArrayList<Temp> temps;
    public ArrayList<Instruction> instructions;
    public ArrayList<String> varNames;

    public IRFunction(String name, Type type) {
        this.name = name;
        this.type = type;
        paraTypes = new ArrayList<Type> ();
        temps = new ArrayList<Temp> ();
        instructions = new ArrayList<Instruction> ();
        varNames = new ArrayList<String> ();
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public String toString() {
        String ret = "FUNC " + name + " (";
        int i = 0;

        // parameters
        if (paraTypes != null) {
            for (Type p : paraTypes) {
                ret += p.toShortString() + " ";
            }
            ret = ret.stripTrailing();
        }
        ret += ")" + type.toShortString() + "\n{\n";

        // print temp declarations 
        // assume paraTypes.size() <= varNames.size() <= temps.size()
        if (temps != null) {
            // print parameters
            for (; i < paraTypes.size(); i++) {
                ret += temps.get(i).declareString(varNames.get(i), true);
            }
            // print locals
            for (; i < varNames.size(); i++) {
                ret += temps.get(i).declareString(varNames.get(i), false);
            }
            // print other temps (real temps)
            for (; i < temps.size(); i++) {
                ret += temps.get(i).declareString();
            }
        }
        ret += "\n";

        // instructions
        if (instructions != null) {
            for (Instruction in : instructions) {
                ret += in.toString() + "\n";
            }
        }
        return ret + "}";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}