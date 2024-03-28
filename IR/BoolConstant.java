package IR;
import Types.Type;
import Types.BooleanType;

public class BoolConstant extends Constant {
    public boolean value;
    BooleanType type;

    public BoolConstant(boolean value) {
        this.value = value;
        type = new BooleanType();
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return value? "TRUE" : "FALSE";
    }
}