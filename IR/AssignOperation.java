package IR;

public class AssignOperation extends Instruction {
    // UL has no unary operators available to user
    public Temp left;
    public BinOperator right;

    public AssignOperation(Temp left, BinOperator right) {
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