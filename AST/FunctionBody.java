package AST;
import java.util.ArrayList;

public class FunctionBody extends NodeObj {
    public VarDeclarationList vars;
    public StatementList stats;
    public ASTNode node = null;

    public FunctionBody(VarDeclarationList varList, StatementList statList) {
        vars = varList;
        stats = statList;
    }

    public ASTNode tree() {
        ASTNode varsNode = vars.tree();
        ASTNode statsNode = stats.tree();

        if (varsNode == null && statsNode == null) {
            return null;
        }
        node = new ASTNode();
        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);
        if (varsNode != null) {
            varsNode.parent = node;
            node.children.add(varsNode);
        }
        if (statsNode != null) {
            statsNode.parent = node;
            node.children.add(statsNode);
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
        return vars.getLine();
    }

    public int getPosition() {
        return vars.getPosition();
    }
}