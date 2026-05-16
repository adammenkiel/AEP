package pl.publicprojects.predictor.model.tester.tests;

import lombok.Getter;
import lombok.Setter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.DataLineContainer;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.tester.AbstractTester;

import java.io.IOException;
import java.util.List;

/**
 * Test for numbers (not vectors) without weights for binary classification
 */
@Getter
public class StandardNumberTest implements AbstractTester<TreeVertex> {

    private final TotalDataContainer totalDataContainer;
    private final Interpreter interpreter;
    @Setter
    private List<VariableData> variables;

    /**
     * @param totalDataContainer TotalDataContainer is required because of using data and parsing into final form
     * @param interpreter Interpreter is used for evaluate math expressions
     */
    public StandardNumberTest(
            TotalDataContainer totalDataContainer,
            Interpreter interpreter
    ) {
        this.totalDataContainer = totalDataContainer;
        this.interpreter = interpreter;
    }

    /**
     * Function for scoring random expressions expressed as graph
     *
     * @param vert Generated random expression
     * @return Score of generated random expression
     * @throws IOException Throws when DataLineContainer have bad types of VariableData,
     * or totalDataContainer is wrong.
     */
    @Override
    public double test(TreeVertex vert) throws IOException {
        int fit = 0;
        int general = 0;

        for (DataLineContainer info : totalDataContainer.getRawData()) {
            final boolean correctResult = ((IntegerNumber) info.getRawData()[0]).getValue() == 1;

            info.update(this.getVariables());
            final double resultDoubleValue = (double) vert.getValue(this.interpreter).getValue();
            final boolean guessResult = resultDoubleValue > 0;
            if (guessResult == correctResult)
                fit += 1;
            general += 1;
        }
        return (double)fit / (double) general;
    }
}
