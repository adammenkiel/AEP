package pl.publicprojects.language.interpreter.data.types.variables.numeric;

import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntegerVariable extends VariableData {

    private final Interpreter interpreter;

    public IntegerVariable(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public IntegerVariable(Interpreter interpreter, int nameId) {
        this.setNameId(nameId);
        this.interpreter = interpreter;
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public void execute() {
        this.interpreter.getCurrentVariables().put(this.getNameId(), this);
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
            integerBytesStream.close();
            dIntegerStream.close();
        } else throw new RuntimeException("Another types isn't supported");
    }

}
