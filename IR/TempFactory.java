package IR;
import java.util.ArrayList;
import Types.*;

public class TempFactory {
    ArrayList<Temp> temps;
    ArrayList<Temp> usedI;
    ArrayList<Temp> usedF;
    ArrayList<Temp> usedC;
    ArrayList<Temp> usedZ;
    ArrayList<Temp> usedU;
    ArrayList<Temp> usedA;

    public TempFactory() {
        temps = new ArrayList<Temp> (65536);
        usedI = new ArrayList<Temp> ();
        usedF = new ArrayList<Temp> ();
        usedC = new ArrayList<Temp> ();
        usedZ = new ArrayList<Temp> ();
        usedU = new ArrayList<Temp> ();
        usedA = new ArrayList<Temp> ();
    }

    public Temp getTemp(Type type, boolean reuse)
            throws Exception {
        Temp temp;
        
        // return a used temp
        if (type instanceof IntType && !usedI.isEmpty()) {
            temp = usedI.remove(0);
        }
        else if (type instanceof FloatType && !usedF.isEmpty()) {
            temp = usedF.remove(0);
        }
        else if (type instanceof CharType && !usedC.isEmpty()) {
            temp = usedC.remove(0);
        }
        else if (type instanceof BooleanType && !usedZ.isEmpty()) {
            temp = usedZ.remove(0);
        }
        else if (type instanceof StringType && !usedU.isEmpty()) {
            temp = usedU.remove(0);
        }
        else if (type instanceof ArrayType && !usedA.isEmpty()) {
            temp = usedA.remove(0);
        }
        else if (type instanceof VoidType) {
            throw new Exception("Void temp is not allowed.");
        }
        // return a new temp
        else {
            if (temps.size() == 65536) {
                throw new Exception("Max number of temps (65536) reached.");
            }
            temp = new Temp(temps.size(), type);
            temps.add(temp);
        }

        // all temps can be reused
        // this boolean means it can be reused in a function
        // i.e., temp is intermediate
        temp.reuse = reuse;
        temp.active = true;
        return temp;
    }

    public void returnTemp(Temp temp) throws Exception {
        if (!temp.active) {
            return;
        }
        Type type = temp.type;

        // put the temp in the right recycle bin
        if (type instanceof IntType) {
            usedI.add(temp);
        }
        else if (type instanceof FloatType) {
            usedF.add(temp);
        }
        else if (type instanceof CharType) {
            usedC.add(temp);
        }
        else if (type instanceof BooleanType) {
            usedZ.add(temp);
        }
        else if (type instanceof StringType) {
            usedU.add(temp);
        }
        else if (type instanceof ArrayType) {
            usedA.add(temp);
        }
        else {
            throw new Exception("Unexpected temp type");
        }
        temp.active = false;
    }

    public void reset() {
        temps.clear();
        usedI.clear();
        usedF.clear();
        usedC.clear();
        usedZ.clear();
        usedU.clear();
        usedA.clear();
    }
}