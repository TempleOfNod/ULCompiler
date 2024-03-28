package AST;
import Types.Type;
import java.util.ArrayList;

public class VarDeclaration extends NodeObj {
    public Type type;
    public Identifier id;
    public ASTNode node = null;

    public VarDeclaration(Type t, Identifier id) {
        type = t;
        this.id = id;
    }

    public ASTNode tree() {
        node = new TypeNode(type);
        ASTNode idNode = id.tree();

        node.obj = this;
        idNode.parent = node;
        node.children = new ArrayList<ASTNode> (1);
        node.children.add(idNode);
        return node;
    }

    public Object accept(Visitor v) throws Exception {
        try {
            return v.visit(this);
        } catch (Exception e) {
            throw e;
        }
    }

    public int getLine() {
        return id.getLine();
    }

    public int getPosition() {
        return id.getPosition();
    }
}