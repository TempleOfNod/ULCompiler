package AST;

public class SemanticException extends Exception {
    public int line;
    public int pos;

    public SemanticException(int line, int pos, String s) {
        super(s);
        this.line = line;
        this.pos = pos;
    }

    public SemanticException(NodeObj o, String s) {
        super(s);
        line = o.getLine();
        pos = o.getPosition();
    }
}