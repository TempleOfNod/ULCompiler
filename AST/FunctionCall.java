package AST;
import java.util.ArrayList;

public class FunctionCall extends Expression {
    public Identifier id;
    public ExpressionList args;
    public ASTNode node = null;

    public FunctionCall(Identifier id, ExpressionList args) {
        this.id = id;
        this.args = args;
    }

    public ASTNode tree() {
        node = new ASTNode();
        ASTNode idNode = id.tree();
        ASTNode argNode = args.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);
        idNode.parent = node;
        node.children.add(idNode);

        if (argNode != null) {
            argNode.parent = node;
            node.children.add(argNode);
        }
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