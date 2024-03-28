package AST;
import java.util.ArrayList;

public class FormalParameterList extends NodeObj {
    public ArrayList<FormalParameter> para;
    public ASTNode node = null;

    public FormalParameterList() {
        para = new ArrayList<FormalParameter> ();
    }

    public void add(FormalParameter p) {
        para.add(p);
    }

    public ASTNode tree() {
        if (para.isEmpty()) {
            return null;
        }

        node = new ASTNode();
        node.obj = this;
        node.children = new ArrayList<ASTNode> ();

        for (FormalParameter p : para) {
            ASTNode pNode = p.tree();
            pNode.parent = node;
            node.children.add(pNode);
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
        if (!para.isEmpty()) {
            return para.get(0).getLine();
        }
        return 0;
    }

    public int getPosition() {
        if (!para.isEmpty()) {
            return para.get(0).getPosition();
        }
        return 0;
    }
}