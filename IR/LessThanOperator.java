package IR;
import Types.BooleanType;

public class LessThanOperator extends BinOperator {

    public LessThanOperator(Temp left, Temp right) {
        super(left, right);
        mark = "<";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}