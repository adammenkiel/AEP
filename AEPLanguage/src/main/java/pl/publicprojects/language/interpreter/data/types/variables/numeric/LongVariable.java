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


public class LongVariable extends VariableData {

    private final Interpreter interpreter;

    public LongVariable(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public void execute() {
        this.setExecuted(true);
        this.interpreter.getCurrentVariables().put(this.getNameId(), this);
    }

    @Override
    public Object getValue() {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(this.getData());
            LanguageInputStream languageInputStream = new LanguageInputStream(this.interpreter, bais);
            return new DoubleNumber(languageInputStream.readLong());
        } catch (Exception ignored) {}
        return null;
    }

    @Override
    public void setValue(Object obj) throws IOException {
        if(obj instanceof LanguageNumber<?> number) {
            Long integer = (Long)number.getValue();
            ByteArrayOutputStream integerBytesStream = new ByteArrayOutputStream();
            DataOutputStream dIntegerStream = new DataOutputStream(integerBytesStream);
            dIntegerStream.writeLong(integer);
            this.setData(integerBytesStream.toByteArray());
        } else throw new RuntimeException("Another types isn't supported");
    }
}
