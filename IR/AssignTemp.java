package IR;

public class AssignTemp extends Instruction {
    public Temp left;
    public Temp right;

    public AssignTemp(Temp left, Temp right) {
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