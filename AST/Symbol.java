package AST;
import Types.Type;

public class Symbol {
    public Type type;
    public Identifier id;

    public Symbol(Type t, Identifier id) {
        type = t;
        this.id = id;
    }
}