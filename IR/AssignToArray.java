package IR;
import Types.Type;

public class AssignToArray extends Instruction {
    public Temp array;
    public Temp index;
    public Temp right;

    public AssignToArray(Temp array, Temp index, Temp right) {
        this.array = array;
        this.index = index;
        this.right = right;
    }

    public String toString() {
        return "\t\t" + array.toString() + "[" + index.toString() 
                + "] := " + right.toString() + ";";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}