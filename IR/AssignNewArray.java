package IR;
import Types.Type;
import Types.ArrayType;

public class AssignNewArray extends Instruction {
    public Temp left;
    public ArrayType atype;

    public AssignNewArray(Temp left, ArrayType atype) {
        this.left = left;
        this.atype = atype;
    }

    public String toString() {
        return "\t\t" + left.toString() + " := NEWARRAY"
                + atype.eleType.toShortString() + " " 
                + Integer.toString(atype.size) + ";";
    }

    public void accept(JasminPrintVisitor v) {
        v.visit(this);
    }
}