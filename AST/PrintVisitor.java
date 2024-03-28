package AST;
import java.util.ArrayList;

public class PrintVisitor extends Visitor {
    int fourSpaces = 0;
    
    public void printSpaces() {
        for (int i = 0; i < fourSpaces; i++) {
            System.out.print("    ");
        }
    }

    public Object visit(AddExpression o) throws Exception {
        o.node.children.get(0).obj.accept(this);
        for (int i = 1; i < o.sign.size(); i++) {
            if (o.sign.get(i)) {
                System.out.print("+");
            }
            else {
                System.out.print("-");
            }
            o.node.children.get(i).obj.accept(this);
        }
        return null;
    }

    public Object visit(ArrayAssignStatement o) throws Exception {
        printSpaces();
        o.node.children.get(0).obj.accept(this);
        System.out.print("=");
        o.node.children.get(1).obj.accept(this);
        System.out.println(";");
        return null;
    }

    public Object visit(ArrayReference o) throws Exception {
        o.node.children.get(0).obj.accept(this);
        System.out.print("[");
        o.node.children.get(1).obj.accept(this);
        System.out.print("]");
        return null;
    }

    public Object visit(AssignStatement o) throws Exception {
        printSpaces();
        o.node.children.get(0).obj.accept(this);
        System.out.print("=");
        o.node.children.get(1).obj.accept(this);
        System.out.println(";");
        return null;
    }

    public Object visit(Block o) throws Exception {
        if (o.node != null) {
            fourSpaces++;
            for (ASTNode n : o.node.children) {
                n.obj.accept(this);
            }
            fourSpaces--;
        }
        return null;
    }

    public Object visit(BoolLiteral o) throws Exception {
        System.out.print(Boolean.toString(o.value));
        return null;
    }

    public Object visit(CharLiteral o) throws Exception {
        System.out.print("'");
        System.out.print(Character.toString(o.value));
        System.out.print("'");
        return null;
    }

    public Object visit(EqualExpression o) throws Exception {
        o.node.children.get(0).obj.accept(this);
        System.out.print("==");
        o.node.children.get(1).obj.accept(this);
        return null;
    }

    public Object visit(ExpressionList o) throws Exception {
        if (o.node == null) {
            return null;
        }
        o.node.children.get(0).obj.accept(this);
        for (int i = 1; i < o.node.children.size(); i++) {
            System.out.print(",");
            o.node.children.get(i).obj.accept(this);
        }

        return null;
    }

    public Object visit(ExpressionStatement o) throws Exception {
        printSpaces();
        o.node.children.get(0).obj.accept(this);
        System.out.println(";");
        return null;
    }

    public Object visit(FloatLiteral o) throws Exception {
        System.out.print(Float.toString(o.value));
        return null;
    }

    public Object visit(FormalParameter o) throws Exception {
        TypeNode t = (TypeNode)o.node;
        System.out.print(t.type.toString());
        System.out.print(" ");
        o.node.children.get(0).obj.accept(this);
        return null;
    }

    public Object visit(FormalParameterList o) throws Exception {
        if (o.node == null) {
            return null;
        }
        o.node.children.get(0).obj.accept(this);
        for (int i = 1; i < o.node.children.size(); i++) {
            System.out.print(", ");
            o.node.children.get(i).obj.accept(this);
        }
        return null;
    }

    public Object visit(Function o) throws Exception {
        o.node.children.get(0).obj.accept(this);
        System.out.print("\n{\n");
        // check if there is a body
        if (o.node.children.size() > 1) {
            o.node.children.get(1).obj.accept(this);
        }
        System.out.print("}");
        return null;
    }

    public Object visit(FunctionBody o) throws Exception {
        fourSpaces++;
        o.node.children.get(0).obj.accept(this);
        if (o.node.children.size() > 1) {
            System.out.println();
            o.node.children.get(1).obj.accept(this);
        }
        fourSpaces--;
        return null;
    }

    public Object visit(FunctionCall o) throws Exception {
        o.node.children.get(0).obj.accept(this);
        System.out.print("(");
        if (o.node.children.size() > 1) {
            o.node.children.get(1).obj.accept(this);
        }
        System.out.print(")");
        return null;
    }

    public Object visit(FunctionDeclaration o) throws Exception {
        TypeNode t = (TypeNode)o.node;

        System.out.print(t.type.toString());
        System.out.print(" ");
        t.children.get(0).obj.accept(this);
        System.out.print(" (");
        if (t.children.size() > 1) {
            t.children.get(1).obj.accept(this);
        }
        System.out.print(")");
        return null;
    }

    public Object visit(Identifier o) throws Exception {
        System.out.print(o.id);
        return null;
    }

    public Object visit(IfStatement o) throws Exception {
        ASTNode ifNode = o.node.children.get(1);

        printSpaces();
        System.out.print("if (");
        o.node.children.get(0).obj.accept(this);
        System.out.println(")");
        printSpaces();
        System.out.println("{");
        if (ifNode != null) {
            ifNode.obj.accept(this);
        }
        printSpaces();
        System.out.println("}");

        // else
        if (o.node.children.size() > 2) {
            printSpaces();
            System.out.println("else");
            printSpaces();
            System.out.println("{");
            o.node.children.get(2).obj.accept(this);
            printSpaces();
            System.out.println("}");
        }        

        return null;
    }

    public Object visit(IntLiteral o) throws Exception {
        System.out.print(Integer.toString(o.value));
        return null;
    }

    public Object visit(LessThanExpression o) throws Exception {
        o.node.children.get(0).obj.accept(this);
        System.out.print("<");
        o.node.children.get(1).obj.accept(this);
        return null;
    }

    public Object visit(MultExpression o) throws Exception {
        o.node.children.get(0).obj.accept(this);
        for (int i = 1; i < o.node.children.size(); i++) {
            System.out.print("*");
            o.node.children.get(i).obj.accept(this);
        }
        return null;
    }

    public Object visit(ParenExpression o) throws Exception {
        System.out.print("(");
        o.node.children.get(0).obj.accept(this);
        System.out.print(")");
        return null;
    }

    public Object visit(PrintLnStatement o) throws Exception {
        printSpaces();
        System.out.print("println ");
        o.node.children.get(0).obj.accept(this);
        System.out.println(";");
        return null;
    }

    public Object visit(PrintStatement o) throws Exception {
        printSpaces();
        System.out.print("print ");
        o.node.children.get(0).obj.accept(this);
        System.out.println(";");
        return null;
    }

    public Object visit(Program o) throws Exception {
        o.node.children.get(0).obj.accept(this);
        for (int i = 1; i < o.node.children.size(); i++) {
            System.out.print("\n\n");
            o.node.children.get(i).obj.accept(this);
        }
        return null;
    }

    public Object visit(ReturnStatement o) throws Exception {
        printSpaces();
        System.out.print("return ");
        if (o.node.children != null) {
            o.node.children.get(0).obj.accept(this);
        }
        System.out.println(";");
        return null;
    }

    public Object visit(StatementList o) throws Exception {
        if (o.node == null) {
            return null;
        }
        for (ASTNode n : o.node.children) {
            n.obj.accept(this);
        }
        return null;
    }

    public Object visit(StrLiteral o) throws Exception {
        System.out.print('"');
        System.out.print(o.value);
        System.out.print('"');
        return null;
    }

    public Object visit(VarDeclaration o) throws Exception {
        TypeNode t = (TypeNode)o.node;
        printSpaces();
        System.out.print(t.type.toString());
        System.out.print(" ");
        t.children.get(0).obj.accept(this);
        System.out.println(";");
        return null;
    }

    public Object visit(VarDeclarationList o) throws Exception {
        if (o.node == null) {
            return null;
        }
        for (ASTNode n : o.node.children) {
            n.obj.accept(this);
        }
        return null;
    }

    public Object visit(WhileStatement o) throws Exception {
        printSpaces();
        System.out.print("while (");
        o.node.children.get(0).obj.accept(this);
        System.out.println(")");
        printSpaces();
        System.out.println("{");
        if (o.node.children.size() > 1) {
            o.node.children.get(1).obj.accept(this);
        }
        printSpaces();
        System.out.println("}");

        return null;
    }
}