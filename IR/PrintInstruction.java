package IR;
import Types.Type;

public class PrintInstruction extends Instruction {
    public Temp temp;

    public PrintInstruction(Temp temp) {
        this.temp = temp;
    }

    public String toString() {
        return "\t\tPRINT" + temp.type.toShortString() + " " + temp.toString() + ";";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}