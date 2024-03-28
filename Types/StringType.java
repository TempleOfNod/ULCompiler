package Types;

public class StringType extends Type {
    public String toString() {
        return "string";
    }

    public String toShortString() {
        return "U";
    }

    public String toJasminString() {
        return "Ljava/lang/String;";
    }

    public char jasminPrefix() {
        return 'a';
    }

	public boolean equals(Object o) {
        return o instanceof StringType;
    }
}