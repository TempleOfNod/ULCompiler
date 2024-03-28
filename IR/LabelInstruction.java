package IR;

public class LabelInstruction extends Instruction {
    public int num;

    public LabelInstruction(int num) {
        this.num = num;
    }

    public String toString() {
        return "L" + Integer.toString(num) + ":;";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}