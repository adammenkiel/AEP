package pl.publicprojects.language.interpreter.data.types.variables.bool;

import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.types.VariableData;

public class BooleanVariable extends VariableData {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void execute() {
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
