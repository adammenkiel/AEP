package pl.publicprojects.language.interpreter.data.types.variables.numeric;

import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DoubleVariable extends VariableData {


    private final Interpreter interpreter;

    public DoubleVariable(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public DoubleVariable(Interpreter interpreter, int nameId) {
        this.interpreter = interpreter;
        this.setNameId(nameId);
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public void execute() {
        this.setExecuted(true);
        this.interpreter.getCurrentVariables().put(this.getNameId(), this);
    }

    @Override
    public Object getValue() {
        try {
            ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(this.getData());
            LanguageInputStream languageInputStream = new LanguageInputStream(this.interpreter, byteArrayStream);
            return new DoubleNumber(languageInputStream.readDouble());
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
        //return null;
    }

    @Override
    public void setValue(Object obj) throws IOException {
        if(obj instanceof LanguageNumber<?> number) {
            Double integer = (Double)number.getValue();
            ByteArrayOutputStream integerBytesStream = new ByteArrayOutputStream();
            DataOutputStream dIntegerStream = new DataOutputStream(integerBytesStream);
            dIntegerStream.writeDouble(integer);
            this.setData(integerBytesStream.toByteArray());
        } else throw new RuntimeException("Another types isn't supported");
    }
}
