package pl.publicprojects.language.interpreter.data.types.variables.numeric;

import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.ByteNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;

import java.io.IOException;


public class ByteVariable extends VariableData {

    private final Interpreter interpreter;

    public ByteVariable(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void execute() {
        this.interpreter.getCurrentVariables().put(this.getNameId(), this);
    }

    @Override
    public Object getValue() {
        return new ByteNumber(this.getData()[0]);
    }

    @Override
    public void setValue(Object obj) throws IOException {
        if(obj instanceof LanguageNumber<?> number) {
            this.setData(new byte[] {(Byte) number.getValue()});
        } else throw new RuntimeException("Another types isn't supported");
    }
}
