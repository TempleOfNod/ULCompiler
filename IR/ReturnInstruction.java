package IR;

public class ReturnInstruction extends Instruction {
    public Temp temp;

    public ReturnInstruction(Temp temp) {
        this.temp = temp;
    }

    public ReturnInstruction() {
        temp = null;
    }

    public String toString() {
        return temp == null?
                "\t\tRETURN;" :
                "\t\tRETURN " + temp.toString() + ";";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}