package AST;
import java.util.ArrayList;

public class EqualExpression extends Expression {
    public Expression left;
    public Expression right;
    public ASTNode node = null;

    public EqualExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public ASTNode tree() {
        node = new ASTNode();
        ASTNode lNode = left.tree();
        ASTNode rNode = right.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);
        lNode.parent = node;
        node.children.add(lNode);
        rNode.parent = node;
        node.children.add(rNode);
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
        return left.getLine();
    }

    public int getPosition() {
        return left.getPosition();
    }
}