package AST;
import java.util.ArrayList;

public class IfStatement extends Statement {
    public Expression cond;
    public Block block;
    public Block elseBlock;
    public boolean hasElse;
    public ASTNode node = null;

    public IfStatement(Expression condition, Block block, Block elseBlock) {
        cond = condition;
        this.block = block;
        this.elseBlock = elseBlock;
        hasElse = elseBlock == null;
    }

    public ASTNode tree() {
        node = new ASTNode();
        ASTNode cNode = cond.tree();
        ASTNode ifNode = block.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (3);
        cNode.parent = node;
        node.children.add(cNode);
        
        if (ifNode != null) {
            ifNode.parent = node;
            node.children.add(ifNode);
        }
        else {
            // in case if block is null and else block is not
            node.children.add(null);
        }
        if (elseBlock != null) {
            ASTNode elseNode = elseBlock.tree();
            if (elseNode != null) {
                elseNode.parent = node;
                node.children.add(elseNode);
            }
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