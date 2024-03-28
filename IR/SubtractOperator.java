package IR;

public class SubtractOperator extends BinOperator {

    public SubtractOperator(Temp left, Temp right) {
        super(left, right);
        mark = "-";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}