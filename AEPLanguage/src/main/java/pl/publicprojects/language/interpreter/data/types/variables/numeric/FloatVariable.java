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

public class FloatVariable extends VariableData {

    private final Interpreter interpreter;

    public FloatVariable(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public int getId() {
        return 5;
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
            return new DoubleNumber(languageInputStream.readFloat());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setValue(Object obj) throws IOException {
        if(obj instanceof LanguageNumber<?> number) {
            Float integer = (Float)number.getValue();
            ByteArrayOutputStream integerBytesStream = new ByteArrayOutputStream();
            DataOutputStream dIntegerStream = new DataOutputStream(integerBytesStream);
            dIntegerStream.writeFloat(integer);
            this.setData(integerBytesStream.toByteArray());
        } else throw new RuntimeException("Another types isn't supported");
    }
}
