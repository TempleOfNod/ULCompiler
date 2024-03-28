package AST;
import java.util.ArrayList;

public class AssignStatement extends Statement {
    public Identifier id;
    public Expression value;
    public ASTNode node = null;

    public AssignStatement(Identifier id, Expression value) {
        this.id = id;
        this.value = value;
    }

    public ASTNode tree() {
        node = new ASTNode();
        ASTNode idNode = id.tree();
        ASTNode vNode = value.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);
        idNode.parent = node;
        node.children.add(idNode);        
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
        return id.getLine();
    }

    public int getPosition() {
        return id.getPosition();
    }
}