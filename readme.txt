===============================================================
Custom Language Compiler
===============================================================
Tommy Zhong
tzrunm@gmail.com

A Java program that compiles a custom programming language
called Unnamed Language (UL) that runs on JVM.

UL is a simple language featuring string and array variables and
if and while satatements. See refmanualv1.4.pdf for more details.

Requires ANTLR3 and Jasmin to run. This compiler parses UL code
input with the former and generates the equivalent Java assembly
to be assembled by the latter. Compatibility with ANTLR4 is not
tested.

https://www.antlr3.org/
https://jasmin.sourceforge.net/

---------------------------------------------------------------
How to Run
---------------------------------------------------------------
1.  Ensure that ANTLR3 is installed.
2.  Compile Java program with makefile.
3.  Run Compiler with .ul filename input. Output would be
    printed in stdout.
4.  To run the UL code, put Compiler output into Jasmin to
    create a .class file.


Example:
make
java Compiler tests/sample.ul > sample.j
java jasmin.Main sample.j
java sample

---------------------------------------------------------------
Architecture
---------------------------------------------------------------
This compiler follows the architecture provided by UVic compiler
construction course. The program is designed with 4 steps:

1.  Tokenize and parse UL code input with ANTLR3. This step is
    also responsible for syntax error detection. The grammar of
    UL is stated in ulGrammar.g.

2.  Construct an abstract syntax tree (AST). Like most (if not
    all) programming languages, UL is a context-free language
    which can be processed in a tree structure. This program
    constructs AST using objects as nodes, their attributes
    as child nodes, and a Program object as root node. After
    constructing AST, a type checker visits the tree to detect
    potential type errors.

3.  Create an intermediate representation (IR) of the assembly
    code. While currently not needed, IR is meant to be
    a common language between different compilers for various
    environments and makes a language more portable. As a low
    level equivalent of a language, IR also allows a compiler
    to make possible optimizations. The IR code can be generated
    using IRProgram.toString().

4.  Output Jasmin assembly. A JasminPrintVisitor object prints
    the assmbly code in stdout. Current implementation of this
    object reads the AST directly.
    
---------------------------------------------------------------
Known Problems
---------------------------------------------------------------
The IR generated in step 3 is yet to be utilized in any way. An
IR optimizer and an IR-to-Assembly printer are planned as
possible features.