package IR;
import java.util.ArrayList;

public class IRProgram {
    public String name;
    public ArrayList<IRFunction> functions;

    public IRProgram(String name) {
        this.name = name;
        functions = new ArrayList<IRFunction> ();
    }

    public void addFunction(IRFunction function) {
        functions.add(function);
    }

    public String toString() {
        String ret = "PROG " + name + "\n";

        for (IRFunction f : functions) {
            ret += f.toString() + "\n\n";
        }
        return ret;
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}