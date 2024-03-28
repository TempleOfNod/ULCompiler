package IR;
import Types.BooleanType;

public class EqualOperator extends BinOperator {

    public EqualOperator(Temp left, Temp right) {
        super(left, right);
        mark = "==";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}