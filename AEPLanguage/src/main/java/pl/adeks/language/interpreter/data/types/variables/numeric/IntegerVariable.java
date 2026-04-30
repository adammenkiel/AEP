package pl.adeks.language.interpreter.data.types.variables.numeric;

import pl.adeks.language.interpreter.Interpreter;
import pl.adeks.language.interpreter.data.math.LanguageNumber;
import pl.adeks.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.adeks.language.interpreter.data.types.VariableData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntegerVariable extends VariableData {

    public IntegerVariable() {}

    public IntegerVariable(int nameId) {
        this.setNameId(nameId);
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public void execute() {
        //Interpreter.getInst() execute save
        Interpreter.getInst().getCurrentVariables().put(this.getNameId(), this);
    }

    @Override
    public Object getValue() {
        byte[] data = this.getData();
        return new IntegerNumber((data[0] & 0xFF) << 24 |
                (data[1] & 0xFF) << 16 |
                (data[2]  & 0xFF) << 8 |
                (data[3]  & 0xFF));
    }

    @Override
    public void setValue(Object obj) throws IOException {
        if(obj instanceof LanguageNumber<?> number) {
            Integer integer = (Integer)number.getValue();
            ByteArrayOutputStream integerBytesStream = new ByteArrayOutputStream();
            DataOutputStream dIntegerStream = new DataOutputStream(integerBytesStream);
            dIntegerStream.writeInt(integer);
            this.setData(integerBytesStream.toByteArray());
        } else throw new RuntimeException("Another types isn't supported");
    }

}
