package IR;

public class IfJumpInstruction extends JumpInstruction {
    public Temp condition;

    public IfJumpInstruction(Temp condition, int target) {
        super(target);
        this.condition = condition;
    }

    public String toString() {
        return "\t\tIF " + condition.toString() + " " 
                + "GOTO L" + Integer.toString(target) + ";";
    }
    
    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}