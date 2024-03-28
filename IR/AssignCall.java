package IR;
import java.util.ArrayList;
import Types.Type;

public class AssignCall extends CallInstruction {
    // unlike other assigns, this is subclass of function call
    public Temp left;

    public AssignCall(Temp left, String name, Type type, ArrayList<Temp> arguments) {
        super(name, type, arguments);
        this.left = left;
    }

    public AssignCall(Temp left, CallInstruction call) {
        super(call.name, call.type, call.arguments);
        this.left = left;
    }

    public String toString() {
        return "\t\t" + left.toString() + " := " + super.toString().substring(2);
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}