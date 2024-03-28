package AST;
import java.util.ArrayList;

public class ReturnStatement extends Statement {
    public Expression expr;
    public ASTNode node = null;
    public int line;
    public int pos;

    public ReturnStatement(Expression expression, int line, int pos) {
        expr = expression;
        // expr can be null so this statement needs its own line & pos
        this.line = line;
        this.pos = pos;
    }

    public ASTNode tree() {
        node = new ASTNode();
        node.obj = this;
        if (expr != null) {
            ASTNode eNode = expr.tree();
            node.children = new ArrayList<ASTNode> (1);
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
        return line;
    }

    public int getPosition() {
        return pos;
    }
}