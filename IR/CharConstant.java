package IR;
import Types.Type;
import Types.CharType;

public class CharConstant extends Constant {
    public char value;
    CharType type;

    public CharConstant(char value) {
        this.value = value;
        type = new CharType();
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return "\'" + value + "\'";
    }
}