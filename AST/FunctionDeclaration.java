package AST;
import java.util.ArrayList;
import Types.Type;

public class FunctionDeclaration extends NodeObj {
    public Type retType;
    public Identifier id;
    public FormalParameterList pl;
    public ASTNode node = null;

    public FunctionDeclaration(Type returnType, Identifier id,
            FormalParameterList paraList) {
        retType = returnType;
        this.id = id;
        pl = paraList;
    }
    
    public ASTNode tree() {
        node = new TypeNode(retType);
        ASTNode idNode = id.tree();
        ASTNode plNode = pl.tree();

        node.obj = this;
        node.children = new ArrayList<ASTNode> (2);
        idNode.parent = node;
        node.children.add(idNode);
        
        if (plNode != null) {
            plNode.parent = node;
            node.children.add(plNode);
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
        return id.getLine();
    }

    public int getPosition() {
        return id.getPosition();
    }
}