package pl.adeks.language.interpreter.data.types.variables.bool;

import pl.adeks.language.interpreter.Interpreter;
import pl.adeks.language.interpreter.data.types.VariableData;

public class BooleanVariable extends VariableData {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void execute() {
        System.out.println("put");
        Interpreter.getInst().getCurrentVariables().put(this.getNameId(), this);
    }

    @Override
    public Object getValue() {
        return this.getData()[0] == 1;
    }

    @Override
    public void setValue(Object obj) {

    }
}
