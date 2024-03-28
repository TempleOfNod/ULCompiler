package AST;
import Types.BooleanType;

public class BoolLiteral extends Expression {
    public final boolean value;
    public final int line;
    public final int pos;
    public ASTNode node = null;

    public BoolLiteral(boolean v, int l, int p) {
        value = v;
        line = l;
        pos = p;
    }

    public ASTNode tree() {
        node = new TypeNode(line, pos, new BooleanType ());
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

    public int getLine() {
        return line;
    }

    public int getPosition() {
        return pos;
    }
}