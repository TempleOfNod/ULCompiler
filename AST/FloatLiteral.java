package AST;
import Types.FloatType;

public class FloatLiteral extends Expression {
    public final float value;
    public final int line;
    public final int pos;
    public ASTNode node = null;

    public FloatLiteral(float v, int l, int p) {
        value = v;
        line = l;
        pos = p;
    }

    public ASTNode tree() {
        node = new TypeNode(line, pos, new FloatType ());
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