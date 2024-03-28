package AST;
import java.util.ArrayList;

public class ArrayAssignStatement extends Statement {
    public ArrayReference ref;
    public Expression value;
    public ASTNode node = null;

    public ArrayAssignStatement(ArrayReference ref, Expression value) {
        this.ref = ref;
        this.value = value;
    }

    public ASTNode tree() {
        node = new ASTNode();
        ASTNode refNode = ref.tree();
        ASTNode vNode = value.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);
        refNode.parent = node;
        node.children.add(refNode);        
        vNode.parent = node;
        node.children.add(vNode);

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
        return ref.getLine();
    }

    public int getPosition() {
        return ref.getPosition();
    }
}