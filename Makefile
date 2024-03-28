#
GNAME= ulGrammar
GSRC= $(GNAME).g

all: types ast ir grammar compiler

ast:
	javac AST/*.java

types:
	javac Types/*.java

ir:
	javac IR/*.java

grammar: $(GSRCS)
	java org.antlr.Tool -fo . $(GSRC) 

compiler:
	javac *.java

clean:
	rm *.class *.ir *.j $(GNAME)*.java $(GNAME).tokens AST/*.class Types/*.class IR/*.class


 
