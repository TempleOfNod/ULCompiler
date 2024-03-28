package IR;

public class AddOperator extends BinOperator {

    public AddOperator(Temp left, Temp right) {
        super(left, right);
        mark = "+";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}