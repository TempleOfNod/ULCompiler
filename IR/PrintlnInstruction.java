package IR;
import Types.Type;

public class PrintlnInstruction extends PrintInstruction {
    public PrintlnInstruction(Temp temp) {
        super(temp);
    }

    public String toString() {
        return "\t\tPRINTLN" + temp.type.toShortString() + " " + temp.toString() + ";";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}