package IR;

public class JumpInstruction extends Instruction {
    public int target;

    public JumpInstruction(int target) {
        this.target = target;
    }

    public String toString() {
        return "\t\tGOTO L" + Integer.toString(target) + ";";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}