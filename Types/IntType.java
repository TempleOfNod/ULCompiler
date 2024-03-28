package Types;

public class IntType extends Type {
    public String toString() {
        return "int";
    }

    public String toShortString() {
        return "I";
    }

    public String toJasminString() {
        return "I";
    }

    public char jasminPrefix() {
        return 'i';
    }

	public boolean equals(Object o) {
        return o instanceof IntType;
    }
}