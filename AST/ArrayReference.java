package AST;
import java.util.ArrayList;

public class ArrayReference extends Expression {
    public Identifier id;
    public Expression in;
    public ASTNode node = null;

    public ArrayReference(Identifier id, Expression index) {
        this.id = id;
        in = index;
    }

    public ASTNode tree() {
        node = new ASTNode();
        ASTNode idNode = id.tree();
        ASTNode inNode = in.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);
        idNode.parent = node;
        node.children.add(idNode);
        inNode.parent = node;
        node.children.add(inNode);
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