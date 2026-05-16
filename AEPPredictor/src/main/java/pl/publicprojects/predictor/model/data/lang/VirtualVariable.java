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

    /**
     * Constructor of fake Variable
     *
     * @param interpreter Interpreter is required to register value in execute.
     * @param nameId ID of variable
     * @param dataPointer That pointer point DataContainer that value we want from,
     *                    it's good method for optimize because we don't need to use loop for update values
     */
    public VirtualVariable(Interpreter interpreter, int nameId, DataPointer dataPointer) {
        this.setNameId(nameId);
        this.pointer = dataPointer;
        this.interpreter = interpreter;
    }

    /**
     * ID of VirtualVariable
     * @return 100
     */
    @Override
    public int getId() {
        return 100;
    }

    @Override
    public void execute() {
        this.interpreter.getCurrentVariables().put(this.getNameId(), this);
    }

    /**
     * As the first value of VirtualDataLineContainer is the results (1/0),
     * value of nameId in variables corresponds to nameId + 1 in VirtualDataLineContainer
     * @return Returns proper LanguageNumber
     */
    @Override
    public Object getValue() {
        try {
            return this.pointer.getPointerContainer().get(this.getNameId() + 1);
        } catch (Exception e) {
            throw new RuntimeException("getPointerContainer#get", e);
        }
    }

    /**
     * Method isn't supported!
     * Method VirtualVariable#set isn't supported as we create get proper value
     * from proper DataLineContainer set in DataPointer
     *
     * @param obj Value to set
     * @throws RuntimeException Throws always.
     */
    @Deprecated
    @Override
    public void setValue(Object obj) throws IOException {
        throw new RuntimeException("That method isn't supported!");
    }
}
