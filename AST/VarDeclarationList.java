package AST;
import java.util.ArrayList;

public class VarDeclarationList extends NodeObj {
    public ArrayList<VarDeclaration> vars;
    public ASTNode node = null;

    public VarDeclarationList() {
        vars = new ArrayList<VarDeclaration> ();
    }

    public void add(VarDeclaration v) {
        vars.add(v);
    }

    public ASTNode tree() {
        if (vars.isEmpty()) {
            return null;
        }

        node = new ASTNode();
        node.obj = this;
        node.children = new ArrayList<ASTNode> ();

        for (VarDeclaration v : vars) {
            ASTNode vNode = v.tree();
            vNode.parent = node;
            node.children.add(vNode);
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
        if (!vars.isEmpty()) {
            return vars.get(0).getLine();
        }
        return 0;
    }

    public int getPosition() {
        if (!vars.isEmpty()) {
            return vars.get(0).getPosition();
        }
        return 0;
    }
}