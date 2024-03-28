package Types;

public class CharType extends Type {
    public String toString() {
        return "char";
    }
    
    public String toShortString() {
        return "C";
    }

    public String toJasminString() {
        return "C";
    }

    public char jasminPrefix() {
        return 'i';
    }

	public boolean equals(Object o) {
        return o instanceof CharType;
    }
}