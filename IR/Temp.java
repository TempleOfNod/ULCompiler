package IR;
import Types.Type;

public class Temp {
    public int num;
    public Type type;
    public boolean reuse;
    public boolean active;

    public Temp(int num, Type type) {
        this.num = num;
        this.type = type;
        reuse = false;
        active = false;
    }

    public String toString() {
        return "T" + Integer.toString(num);
    }

    public String declareString(String name, boolean paraOrLocal) {
        String ret = "\tTEMP " + num + ":" + type.toShortString();
        // add name to parameter or variable
        if (name != null) {
            ret += paraOrLocal? "  [P(\"" : "  [L(\"";
            ret += name + "\")]";
        }
        return ret + ";\n";
    }

    public String declareString() {
        return declareString(null, false);
    }
}