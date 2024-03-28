/*
 * Compiler.java
 *
 * A starting place for the unnamed language compiler for CSC 435/535
 *
 */

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.io.*;
import AST.*;
import Types.*;
import IR.*;

public class Compiler {
	public static void main (String[] args) throws Exception {
		ANTLRInputStream input;

		if (args.length == 0 ) {
			System.out.println("Usage: Compiler filename.ul");
			return;
		}
		else {
			input = new ANTLRInputStream(new FileInputStream(args[0]));
		}

		// The name of the grammar here is "ulGrammar",
		// so ANTLR generates ulGrammarLexer and ulGrammarParser
		ulGrammarLexer lexer = new ulGrammarLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ulGrammarParser parser = new ulGrammarParser(tokens);

		String filename = args[0].substring(
				args[0].lastIndexOf('/') + 1, args[0].length() - 3);

		TypeVisitor typeChecker = new TypeVisitor();
		IRVisitor irVisitor = new IRVisitor(filename);
		JasminPrintVisitor jVisitor = new JasminPrintVisitor();

		try {
			Program p = parser.program();
			p.accept(typeChecker);
			
			IRProgram ir = (IRProgram)(p.accept(irVisitor));
			ir.accept(jVisitor);
		}
		catch (RecognitionException e )	{
			// A lexical or parsing error occured.
			// ANTLR will have already printed information on the
			// console due to code added to the grammar.  So there is
			// nothing to do here.
		}
		catch (SemanticException e) {
			// Type checking errors
			System.out.println("Type or semantic error");
			System.out.print("line ");
			System.out.print(e.line);
			System.out.println(":\t" + e.getMessage());
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
