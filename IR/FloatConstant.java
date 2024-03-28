package IR;
import Types.Type;
import Types.FloatType;

public class FloatConstant extends Constant {
    public float value;
    FloatType type;

    public FloatConstant(float value) {
        this.value = value;
        type = new FloatType();
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return Float.toString(value);
    }
}