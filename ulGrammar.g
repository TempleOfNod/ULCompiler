grammar ulGrammar;

@header {
        import AST.*;
        import Types.*;
}

@members
{
protected void mismatch (IntStream input, int ttype, BitSet follow)
        throws RecognitionException
{
        throw new MismatchedTokenException(ttype, input);
}
public Object recoverFromMismatchedSet (IntStream input,
                                      RecognitionException e,
                                      BitSet follow)
        throws RecognitionException
{
        reportError(e);
        throw e;
}
}

@rulecatch {
        catch (RecognitionException ex) {
                reportError(ex);
                throw ex;
        }
}

program returns [Program p]
        @init { p = new Program(); }:
        (f = function { p.add(f); })+ EOF
	;

function returns [Function f]:
        fd = functionDecl fb = functionBody {
                f = new Function(fd, fb);
        }
	;

functionDecl returns [FunctionDeclaration fd]:
        t = compoundType id = identifier LPAREN pl = formalParameterList RPAREN {
                fd = new FunctionDeclaration(t, id, pl);
        }
	;

formalParameter returns [FormalParameter p]:
        t = compoundType id = identifier {
                p = new FormalParameter(t, id);
        }
        ;

formalParameterList returns [FormalParameterList pl]
        @init { pl = new FormalParameterList(); }:
        (f = formalParameter { pl.add(f); } 
        (m = moreFormals { pl.add(m); })*)?
        ;

moreFormals returns [FormalParameter p]:
        COMMA f = formalParameter { p = f; }
        ;

functionBody returns [FunctionBody fb]:
        LBRACE vl = varDeclList sl = statementList RBRACE {
                fb = new FunctionBody(vl, sl);
        }
	;

varDecl returns [VarDeclaration v]:
        t = compoundType id = identifier SEMICOLON {
                v = new VarDeclaration(t, id);
        }
        ;

varDeclList returns [VarDeclarationList vl]
        @init { vl = new VarDeclarationList(); }:
        (v = varDecl { vl.add(v); })*
        ;

compoundType returns [Type t]
        @after { t = ty; }:
          ty = arrayType
        | ty = type
        ;

// types
arrayType returns [Type t]:
        et = type LSQUARE s = intLit RSQUARE {
                t = new ArrayType(et, s);
        };

type returns [Type t]:
          INT           { t = new IntType(); }
        | FLOAT         { t = new FloatType(); }
        | BOOLEAN       { t = new BooleanType(); }
        | CHAR          { t = new CharType(); }
        | STRING        { t = new StringType(); }
        | VOID          { t = new VoidType(); }
        ;

// statements
// Is there a way to not backtrack?
/*      rule statement has non-LL(*) decision due to 
        recursive rule invocations reachable from alts 2,8.
*/
statement returns [Statement s] options {backtrack=true;}
        @init { stat = null; }
        @after { s = stat; }:
          SEMICOLON
        | stat = exprStatement
        | stat = ifStatement
        | stat = whileStatement
        | stat = printStatement
        | stat = printlnStatement
        | stat = returnStatement
        | stat = arrayAssignStatement
        | stat = assignStatement
        ;

statementList returns [StatementList sl]
        @init { sl = new StatementList(); }:
        (s = statement { sl.add(s); })*
        ;

exprStatement returns [Statement s]:
        e = expr SEMICOLON { s = new ExpressionStatement(e); }
        ;

printStatement returns [Statement s]:
        PRINT e = expr SEMICOLON { s = new PrintStatement(e); }
        ;

printlnStatement returns [Statement s]:
        PRINTLN e = expr SEMICOLON { s = new PrintLnStatement(e); }
        ;

returnStatement returns [Statement s]
        @init { e = null; }:
        r = RETURN (e = expr)? SEMICOLON {
                s = new ReturnStatement(
                        e,
                        r.getLine(),
                        r.getCharPositionInLine());
        }
        ;

ifStatement returns [Statement s]
        @init { b2 = null; }
        @after { s = new IfStatement(c, b1, b2); }:
        IF LPAREN c = expr RPAREN b1 = block (ELSE b2 = block)?
        ;

whileStatement returns [Statement s]:
        WHILE LPAREN c = expr RPAREN b = block {
                s = new WhileStatement(c, b);
        }
        ;

assignStatement returns [Statement s]:
        i = identifier ASSIGN e = expr SEMICOLON {
                s = new AssignStatement(i, e);
        }
        ;

arrayAssignStatement returns [Statement s]:
        i = arrayRef ASSIGN e = expr SEMICOLON {
                s = new ArrayAssignStatement(i, e);
        }
        ;

block returns [Block b]:
        LBRACE sl = statementList { b = new Block(sl); } RBRACE
        ;

// expressions
expr returns [Expression e]:
        l = lessExpr { e = l; }
        (EQUAL r = lessExpr {
                e = new EqualExpression(l, r);
        })?
        ;

lessExpr returns [Expression e]:
        l = addExpr { e = l; }
        (LESSTHAN r = addExpr {
                e = new LessThanExpression(l, r);
        })?
        ;

addExpr returns [Expression e] // for substraction as well
        @init {
                AddExpression p = new AddExpression();
                boolean s = true;
        }:
        a = multExpr {
                e = a;
                p.add(a, true);
        }
        (((PLUS { s = true; } | MINUS { s = false; })
                b = multExpr { p.add(b, s); })+ { e = p; })?
        ;

multExpr returns [Expression e]
        @init { MultExpression m = new MultExpression(); }:
        a = atom {
                e = a;
                m.add(a);
        } 
        ((MULTIPLY b = atom { m.add(b); })+ { e = m; })?
        ;

atom returns [Expression e]
        @after { e = a; }:
          a = arrayRef
        | a = functionCall
        | a = identifier
        | a = literal
        | a = parenExpr
        ;

functionCall returns [Expression e]:
        i = identifier LPAREN l = exprList RPAREN {
                e = new FunctionCall(i, l);
        }
        ;

exprList returns [ExpressionList l]
        @init { l = new ExpressionList(); }:
        (a = expr { l.add(a); } (b = exprMore { l.add(b); })*)?
        ;

exprMore returns [Expression e]:
        COMMA m = expr { e = m; }
        ;

parenExpr returns [Expression e]:
        LPAREN exp = expr RPAREN { e = new ParenExpression(exp); }
        ;

arrayRef returns [ArrayReference e]:
        id = identifier LSQUARE i = expr RSQUARE {
                e = new ArrayReference(id, i);
        }
        ;

identifier returns [Identifier id]:
        i = ID { id = new Identifier(
                        i.getText(),
                        i.getLine(),
                        i.getCharPositionInLine()); }
	;

// literals
literal returns [Expression e]:
          i = intLit    { e = i; }
        | f = floatLit  { e = f; }
        | c = charLit   { e = c; }
        | s = strLit    { e = s; }
        | b = boolLit   { e = b; }
        ;

intLit returns [IntLiteral l]:
        i = INTLIT {
                l = new IntLiteral(
                        Integer.parseInt(i.getText()),
                        i.getLine(),
                        i.getCharPositionInLine());
        }
        ;

floatLit returns [FloatLiteral l]:
        f = FLOATLIT {
                l = new FloatLiteral(
                        Float.parseFloat(f.getText()),
                        f.getLine(),
                        f.getCharPositionInLine());
        }
        ;

charLit returns [CharLiteral l]:
        c = CHARLIT {
                l = new CharLiteral(
                        c.getText().charAt(1),
                        c.getLine(),
                        c.getCharPositionInLine());
        }
        ;

strLit returns [StrLiteral l]:
        s = STRLIT {
                l = new StrLiteral(
                        s.getText().substring(1, s.getText().length() - 1),
                        s.getLine(),
                        s.getCharPositionInLine());
        }
        ;

boolLit returns [BoolLiteral l]:
        b = (TRUE | FALSE) {
                l = new BoolLiteral(
                        Boolean.parseBoolean(b.getText()),
                        b.getLine(),
                        b.getCharPositionInLine());
        }
        ;

/* Lexer */
	 
IF	: 'if'
	;
ELSE    : 'else'
        ;
WHILE   : 'while'
        ;

PRINT   : 'print'
        ;
PRINTLN : 'println'
        ;
RETURN  : 'return'
        ;

TRUE    : 'true'
        ;
FALSE   : 'false'
        ;

// brackets
LBRACE  : '{'
        ;
RBRACE  : '}'
        ;
LPAREN  : '('
        ;
RPAREN  : ')'
        ;
LSQUARE : '['
        ;
RSQUARE : ']'
        ;

// operators
EQUAL   : '=='
        ;
ASSIGN  : '='
        ;
LESSTHAN: '<'
        ;
PLUS    : '+'
        ;
MINUS   : '-'
        ;
MULTIPLY: '*'
        ;

// symbols
COMMA   : ','
        ;
SEMICOLON: ';'
        ;

// types
INT     : 'int'
        ;
FLOAT   : 'float'
        ;
BOOLEAN : 'boolean'
        ;
CHAR    : 'char'
        ;
STRING  : 'string'
        ;
VOID    : 'void'
        ;

// One letter followed by any number of letters and/or numbers
ID	: (('a'..'z') | ('A'..'Z') | '_') (('a'..'z') | ('A'..'Z') | ('0'..'9') | '_')*
	;

// literals
INTLIT  : ('0' | ('1'..'9'('0'..'9')*))
        ;
FLOATLIT: ('0'..'9')+'.'('0'..'9')+('('('0'..'9')+')')?
        ;
CHARLIT : '\u0027' (CHARS | SPACE) '\u0027'
        ;
STRLIT  : '\u0022' (CHARS | SPACE)* '\u0022'
        ;

CHARS   : ('a'..'z')
        | ('A'..'Z')
        | ('0'..'9')
        | COMMA | LBRACE | RBRACE
        | '!' | '.' | ':' | '_'
        ;

SPACE   : ' ' { $channel = HIDDEN;}
        ;

WS      : ( '\t' | ('\r' | '\n') )+ { $channel = HIDDEN;}
        ;

COMMENT : '//' ~('\r' | '\n')* ('\r' | '\n') { $channel = HIDDEN;}
        ;
