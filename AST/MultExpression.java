package AST;
import java.util.ArrayList;

public class MultExpression extends Expression {
    public ArrayList<Expression> exprs;
    public ASTNode node = null;

    public MultExpression() {
        exprs = new ArrayList<Expression> ();
    }

    public void add(Expression e) {
        exprs.add(e);
    }

    public ASTNode tree() {
        node = new ASTNode();
        node.obj = this;
        node.children = new ArrayList<ASTNode> ();
        for (Expression e : exprs) {
            ASTNode cNode = e.tree();
            cNode.parent = node;
            node.children.add(cNode);
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
        return exprs.get(0).getLine();
    }

    public int getPosition() {
        return exprs.get(0).getPosition();
    }
}