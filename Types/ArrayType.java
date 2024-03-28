package Types;
import AST.IntLiteral;

public class ArrayType extends Type {
    public Type eleType;
    public int size;

    public ArrayType(Type elementType, IntLiteral size) {
        eleType = elementType;
        this.size = size.value;
    }

    public String toString() {
        return eleType.toString() + "[" 
                + Integer.toString(size)  + "]";
    }

    public String toShortString() {
        return "A" + eleType.toShortString();
    }

    public String toJasminString() {
        return "[" + eleType.toJasminString();
    }

    public char jasminPrefix() {
        return 'a';
    }

	public boolean equals(Object o) {
        if (o instanceof ArrayType) {
            ArrayType other = (ArrayType)o;
            return eleType.equals(other.eleType);
        }
        return false;
    }
}