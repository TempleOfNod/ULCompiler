package AST;
import Types.Type;

public class TypeNode extends ASTNode {
	public Type type;

    public TypeNode(Type type) {
        super();
        this.type = type;
    }

    public TypeNode (int l, int o, Type type) {
        super(l, o);
        this.type = type;
    }
}