package Types;

public class FloatType extends Type {
    public String toString() {
        return "float";
    }

    public String toShortString() {
        return "F";
    }

    public String toJasminString() {
        return "F";
    }

    public char jasminPrefix() {
        return 'f';
    }

	public boolean equals(Object o) {
        return o instanceof FloatType;
    }
}