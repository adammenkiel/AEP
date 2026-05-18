package pl.publicprojects.predictor.model.data.container.total;

import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.model.data.TotalDataContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DoubleTotalDataContainer extends TotalDataContainer {

    private final Interpreter interpreter;

    /**
     * @param interpreter Required for register variables
     */
    public DoubleTotalDataContainer(Interpreter interpreter) {
        this.interpreter = interpreter;
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
            DoubleVariable variable = new DoubleVariable(this.interpreter, nameId);
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
        DoubleVariable variable = new DoubleVariable(this.interpreter, nameId);
        variable.execute();
        variable.setValue(new DoubleNumber(0));
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
