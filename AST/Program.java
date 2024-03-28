package AST;
import java.util.ArrayList;

public class Program extends NodeObj {
    public ArrayList<Function> funcs;
    public ASTNode node = null;

    public Program() {
        funcs = new ArrayList<Function> ();
    }

    public void add(Function f) {
        funcs.add(f);
    }

    public ASTNode tree() {
        node = new ASTNode();
        node.children = new ArrayList<ASTNode> ();

        node.obj = this;
        for (Function f : funcs) {
            ASTNode child = f.tree();
            child.parent = node;
            node.children.add(child);
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
        return 0;
    }

    public int getPosition() {
        return 0;
    }
}