package pl.publicprojects.predictor.model.data.lang;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;

import java.io.IOException;

/**
 * Fake Variable for avoid loops inside DataContainer
 */
@Getter
public class VirtualVariable extends VariableData {

    private final DataPointer pointer;
    private final Interpreter interpreter;

    public VirtualVariable(Interpreter interpreter, int nameId, DataPointer dataPointer) {
        this.setNameId(nameId);
        this.pointer = dataPointer;
        this.interpreter = interpreter;
    }

    @Override
    public int getId() {
        return 100;
    }

    @Override
    public void execute() {
        this.interpreter.getCurrentVariables().put(this.getNameId(), this);
    }

    @Override
    public Object getValue() {
        try {
            return this.pointer.getPointerContainer().get(this.getNameId() + 1);
        } catch (Exception e) {
            throw new RuntimeException("getPointerContainer#get", e);
        }
    }

    @Override
    public void setValue(Object obj) throws IOException {
        throw new RuntimeException("That method isn't supported!");
    }
}
