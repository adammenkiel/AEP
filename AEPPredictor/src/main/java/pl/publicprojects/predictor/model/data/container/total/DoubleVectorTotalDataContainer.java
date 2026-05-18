package pl.publicprojects.predictor.model.data.container.total;

import lombok.Getter;
import org.nd4j.linalg.factory.Nd4j;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleVectorNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVectorVariable;
import pl.publicprojects.predictor.model.data.TotalDataContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DoubleVectorTotalDataContainer extends TotalDataContainer {

    private final Interpreter interpreter;
    private final int dataLinesLength;
    /**
     * @param interpreter Required for register variables
     * @param dataLinesLength Required for "parse" numbers into vectors
     */
    public DoubleVectorTotalDataContainer(Interpreter interpreter, int dataLinesLength) {
        this.interpreter = interpreter;
        this.dataLinesLength = dataLinesLength;
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
            DoubleVectorVariable variable = new DoubleVectorVariable(interpreter, nameId, new ArrayList<>());
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
        DoubleVectorVariable variable = new DoubleVectorVariable(interpreter, nameId, new ArrayList<>());
        variable.execute();
        return variable;
    }

    /**
     * Changes var Number into DoubleVector
     * @param var Language number with arbitrary type
     * @return Returns number parsed into double vector
     */
    @Override
    public LanguageNumber<?> standardize(LanguageNumber<?> var) {
        return var.plus(new DoubleVectorNumber(Nd4j.zeros(this.dataLinesLength)));
    }
}
