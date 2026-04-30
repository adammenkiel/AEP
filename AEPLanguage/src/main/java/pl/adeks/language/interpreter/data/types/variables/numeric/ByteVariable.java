package pl.adeks.language.interpreter.data.types.variables.numeric;

import pl.adeks.language.interpreter.Interpreter;
import pl.adeks.language.interpreter.data.math.LanguageNumber;
import pl.adeks.language.interpreter.data.types.VariableData;

import java.io.IOException;

@Deprecated
public class ByteVariable extends VariableData {
    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void execute() {
        Interpreter.getInst().getCurrentVariables().put(this.getNameId(), this);
    }

    @Override
    public Object getValue() {
        return this.getData()[0];
    }

    @Override
    public void setValue(Object obj) throws IOException {
        if(obj instanceof LanguageNumber<?> number) {
            this.setData(new byte[] {(Byte) number.getValue()});
        } else throw new RuntimeException("Another types isn't supported");
    }
}
