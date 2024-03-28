package IR;

public class AssignConstant extends Instruction {
    public Temp left;
    public Constant right;

    public AssignConstant(Temp left, Constant right) {
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return "\t\t" + left.toString() + " := " + right.toString() + ";";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}