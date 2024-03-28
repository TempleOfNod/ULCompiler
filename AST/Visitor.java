package AST;

public abstract class Visitor {
    public abstract Object visit(AddExpression o) throws Exception;
    public abstract Object visit(ArrayAssignStatement o) throws Exception;
    public abstract Object visit(ArrayReference o) throws Exception;
    public abstract Object visit(AssignStatement o) throws Exception;
    public abstract Object visit(Block o) throws Exception;
    public abstract Object visit(BoolLiteral o) throws Exception;
    public abstract Object visit(CharLiteral o) throws Exception;
    public abstract Object visit(EqualExpression o) throws Exception;
    public abstract Object visit(ExpressionList o) throws Exception;
    public abstract Object visit(ExpressionStatement o) throws Exception;
    public abstract Object visit(FloatLiteral o) throws Exception;
    public abstract Object visit(FormalParameter o) throws Exception;
    public abstract Object visit(FormalParameterList o) throws Exception;
    public abstract Object visit(Function o) throws Exception;
    public abstract Object visit(FunctionBody o) throws Exception;
    public abstract Object visit(FunctionCall o) throws Exception;
    public abstract Object visit(FunctionDeclaration o) throws Exception;
    public abstract Object visit(Identifier o) throws Exception;
    public abstract Object visit(IfStatement o) throws Exception;
    public abstract Object visit(IntLiteral o) throws Exception;
    public abstract Object visit(LessThanExpression o) throws Exception;
    public abstract Object visit(MultExpression o) throws Exception;
    public abstract Object visit(ParenExpression o) throws Exception;
    public abstract Object visit(PrintLnStatement o) throws Exception;
    public abstract Object visit(PrintStatement o) throws Exception;
    public abstract Object visit(Program o) throws Exception;
    public abstract Object visit(ReturnStatement o) throws Exception;
    public abstract Object visit(StatementList o) throws Exception;
    public abstract Object visit(StrLiteral o) throws Exception;
    public abstract Object visit(VarDeclaration o) throws Exception;
    public abstract Object visit(VarDeclarationList o) throws Exception;
    public abstract Object visit(WhileStatement o) throws Exception;
}