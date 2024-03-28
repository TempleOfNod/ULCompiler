package AST;
import java.util.ArrayList;

public class WhileStatement extends Statement {
    public Expression cond;
    public Block block;
    public ASTNode node = null;

    public WhileStatement(Expression condition, Block block) {
        cond = condition;
        this.block = block;
    }

    public ASTNode tree() {
        node = new ASTNode();
        ASTNode cNode = cond.tree();
        ASTNode bNode = block.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);    
        cNode.parent = node;
        node.children.add(cNode);
        
        if (bNode != null) {
            bNode.parent = node;
            node.children.add(bNode);
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
        return cond.getLine();
    }

    public int getPosition() {
        return cond.getPosition();
    }
}