package IR;

public class MultOperator extends BinOperator {
    
    public MultOperator(Temp left, Temp right) {
        super(left, right);
        mark = "*";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}