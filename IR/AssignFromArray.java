package IR;
import Types.Type;

public class AssignFromArray extends Instruction {
    public Temp left;
    public Temp array;
    public Temp index;

    public AssignFromArray(Temp left, Temp array, Temp index) {
        this.left = left;
        this.array = array;
        this.index = index;
    }

    public String toString() {
        return "\t\t" + left.toString() + " := "
                + array.toString() + "[" + index.toString() + "];";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}