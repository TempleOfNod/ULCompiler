package AST;

public abstract class NodeObj {
    public abstract ASTNode tree();
    public abstract Object accept(Visitor v) throws Exception;
    public abstract int getLine();
    public abstract int getPosition();
}