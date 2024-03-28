package IR;
import Types.Type;
import Types.StringType;

public class StrConstant extends Constant {
    public String value;
    StringType type;

    public StrConstant(String value) {
        this.value = value;
        type = new StringType();
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return "\"" + value + "\"";
    }
}