package IR;

public abstract class Instruction {
    public abstract void accept(JasminPrintVisitor v);
}