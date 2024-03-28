package Types;

public class BooleanType extends Type {
    public String toString() {
        return "boolean";
    }

    public String toShortString() {
        return "Z";
    }

    public String toJasminString() {
        return "Z";
    }

    public char jasminPrefix() {
        return 'i';
    }

	public boolean equals(Object o) {
        return o instanceof BooleanType;
    }
}