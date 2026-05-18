package pl.publicprojects.predictor.model.data.container.total;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.lang.DataPointer;
import pl.publicprojects.predictor.model.data.lang.VirtualVariable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class VirtualTotalDataContainer extends TotalDataContainer {

    private final DataPointer pointer;
    private final Interpreter interpreter;

    /**
     * @param interpreter Required for register variables
     * @param pointer Virtual variables should returns values of pointer DataContainer
     */
    public VirtualTotalDataContainer(Interpreter interpreter, DataPointer pointer) {
        this.interpreter = interpreter;
        this.pointer = pointer;
    }

    /**
     * Creates begin variables
     * @param dataSize Size of initial data columns
     * @return Returns list of begin variables
     */
    @Override
    public List<VariableData> createVariables(int dataSize) {
        List<VariableData> list = new ArrayList<>();
        for(int nameId = 0; nameId < dataSize; nameId++) {
            VirtualVariable variable = new VirtualVariable(interpreter, nameId, pointer);
            variable.execute();
            list.add(variable);
        }
        return list;
    }

    /**
     * Creates a single variable
     * @param nameId nameId of new variable
     * @return Returns variable with nameId id
     */
    @Override
    public VariableData createVariable(int nameId) throws IOException {
        VirtualVariable variable = new VirtualVariable(interpreter, nameId, pointer);
        variable.execute();
        return variable;
    }

    /**
     * Changes var Number into DoubleNumber
     * @param var Language number with arbitrary type
     * @return Returns number parsed into double
     */
    @Override
    public LanguageNumber<?> standardize(LanguageNumber<?> var) {
        return var.plus(new DoubleNumber(0));
    }

}
