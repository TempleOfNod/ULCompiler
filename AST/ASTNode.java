package AST;
import java.util.ArrayList;

// Old and obsolete tree node class
// Currently serves no purpose since child nodes don't need to
// know parents and parent nodes store children in attributes
public class ASTNode {
	public int line;
	public int offset;
	public NodeObj obj;
	public ASTNode parent;
	public ArrayList<ASTNode> children;

	public ASTNode() {
		line = -1;
		offset = -1;
		obj = null;
		parent = null;
		children = null;
	}

	public ASTNode (int l, int o) {
		line = l;
		offset = o;
		obj = null;
		parent = null;
		children = null;
	}
}