package AST;
import java.util.ArrayList;

public class AddExpression extends Expression {
    public ArrayList<Expression> exprs;   // expressions/values to add
    public ArrayList<Boolean> sign;      // signs/operators
    public ASTNode node = null;

    public AddExpression() {
        exprs = new ArrayList<Expression> ();
        sign = new ArrayList<Boolean> ();
    }

    public void add(Expression e, boolean sign) {
        exprs.add(e);
        this.sign.add(sign);
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