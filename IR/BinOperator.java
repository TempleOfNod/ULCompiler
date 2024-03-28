package IR;
import Types.Type;

public abstract class BinOperator {
    public Temp left;
    public Temp right;
    Type type;
    String mark;

    public BinOperator(Temp left, Temp right) {
        this.left = left;
        this.right = right;
        type = left.type;
        mark = "o";
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return left.toString() + " " + type.toShortString()
                + mark + " " + right.toString();
    }

    public abstract void accept(JasminPrintVisitor v);
}