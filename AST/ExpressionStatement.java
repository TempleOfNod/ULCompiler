package AST;
import java.util.ArrayList;

public class ExpressionStatement extends Statement {
    public Expression expr;
    public ASTNode node = null;

    public ExpressionStatement(Expression expression) {
        expr = expression;
    }

    public ASTNode tree() {
        node = new ASTNode();
        ASTNode eNode = expr.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (1);
        eNode.parent = node;
        node.children.add(eNode);
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
        return expr.getLine();
    }

    public int getPosition() {
        return expr.getPosition();
    }
}