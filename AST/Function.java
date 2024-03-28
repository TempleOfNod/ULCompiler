package AST;
import java.util.ArrayList;

public class Function extends NodeObj {
    public FunctionDeclaration decl;
    public FunctionBody body;
    public ASTNode node = null;

    public Function(FunctionDeclaration declaration, FunctionBody body) {
        decl = declaration;
        this.body = body;
    }

    public ASTNode tree() {
        node = new ASTNode();
        ASTNode declNode = decl.tree();
        ASTNode bodyNode = body.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);
        declNode.parent = node;
        node.children.add(declNode);
        
        if (bodyNode != null) {
            bodyNode.parent = node;
            node.children.add(bodyNode);
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
        return decl.getLine();
    }

    public int getPosition() {
        return decl.getPosition();
    }
}