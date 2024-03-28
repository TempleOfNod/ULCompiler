package AST;
import java.util.ArrayList;

public class ExpressionList extends NodeObj {
    public ArrayList<Expression> exprs;
    public ASTNode node = null;

    public ExpressionList() {
        exprs = new ArrayList<Expression> ();
    }

    public void add(Expression e) {
        exprs.add(e);
    }

    public ASTNode tree() {
        if (exprs.isEmpty()) {
            return null;
        }

        node = new ASTNode();
        node.obj = this;
        node.children = new ArrayList<ASTNode> ();

        for (Expression e : exprs) {
            ASTNode eNode = e.tree();
            eNode.parent = node;
            node.children.add(eNode);
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
        if (!exprs.isEmpty()) {
            return exprs.get(0).getLine();
        }
        return 0;
    }

    public int getPosition() {
        if (!exprs.isEmpty()) {
            return exprs.get(0).getPosition();
        }
        return 0;
    }
}