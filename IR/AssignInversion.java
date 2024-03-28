package IR;

public class AssignInversion extends Instruction {
    // used internally as part of if instruction
    public Temp left;
    public Temp right;

    public AssignInversion(Temp left, Temp right) {
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return "\t\t" + left.toString() + " := Z! " + right.toString() + ";";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}