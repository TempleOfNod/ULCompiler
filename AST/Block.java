package AST;
import java.util.ArrayList;

public class Block extends NodeObj {
    public StatementList sl;
    public ASTNode node = null;

    public Block(StatementList statementList) {
        sl = statementList;
    }

    public ASTNode tree() {
        ASTNode slNode = sl.tree();
        if (slNode == null) {
            return null;
        }
        node = new ASTNode();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);
        slNode.parent = node;
        node.children.add(slNode);
        
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
        return sl.getLine();
    }

    public int getPosition() {
        return sl.getPosition();
    }
}