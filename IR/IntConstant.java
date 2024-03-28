package IR;
import Types.Type;
import Types.IntType;

public class IntConstant extends Constant {
    public int value;
    IntType type;

    public IntConstant(int value) {
        this.value = value;
        type = new IntType();
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return Integer.toString(value);
    }
}