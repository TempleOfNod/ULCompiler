package AST;
import Types.Type;
import java.util.ArrayList;

public class FunctionSymbol extends Symbol {
    public ArrayList<Symbol> paraSymbols;

    public FunctionSymbol(Type t, Identifier id, ArrayList<Symbol> para) {
        super(t, id);
        paraSymbols = para;
    }
}