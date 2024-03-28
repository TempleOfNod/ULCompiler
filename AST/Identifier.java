package AST;

public class Identifier extends Expression {
    public String id;
    public ASTNode node = null;
    public int line;
    public int pos;

    public Identifier(String id, int line, int pos) {
        this.id = id;
        this.line = line;
        this.pos = pos;
    }

    public ASTNode tree() {
        node = new ASTNode();
        node.obj = this;
        return node;
    }

    public Object accept(Visitor v) throws Exception {
        try {
            return v.visit(this);
        } catch (Exception e) {
            throw e;
        }
    }

    public String toString() {
        return id;
    }

    public int getLine() {
        return line;
    }

    public int getPosition() {
        return pos;
    }
}