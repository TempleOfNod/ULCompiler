package Types;

public abstract class Type {
	public abstract String toShortString();
	public abstract String toJasminString();
	public abstract char jasminPrefix();
	public abstract boolean equals (Object o);
}