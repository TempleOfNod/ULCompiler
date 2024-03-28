package Types;

public class VoidType extends Type {
    public String toString() {
        return "void";
    }

    public String toShortString() {
        return "V";
    }

    public String toJasminString() {
        return "V";
    }

    public char jasminPrefix() {
        return '?';    // should not be called
    }

	public boolean equals(Object o) {
        return o instanceof VoidType;
    }
}