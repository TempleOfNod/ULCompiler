package AST;
import java.util.ArrayList;

public class StatementList extends NodeObj {
    public ArrayList<Statement> stats;
    public ASTNode node = null;

    public StatementList() {
        stats = new ArrayList<Statement> ();
    }

    public void add(Statement s) {
        if (s != null) {
            stats.add(s);
        }
    }

    public ASTNode tree() {
        if (stats.isEmpty()) {
            return null;
        }

        node = new ASTNode();
        node.obj = this;
        node.children = new ArrayList<ASTNode> ();

        for (Statement s : stats) {
            ASTNode sNode = s.tree();
            sNode.parent = node;
            node.children.add(sNode);
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
        if (!stats.isEmpty()) {
            return stats.get(0).getLine();
        }
        return 0;
    }

    public int getPosition() {
        if (!stats.isEmpty()) {
            return stats.get(0).getPosition();
        }
        return 0;
    }
}