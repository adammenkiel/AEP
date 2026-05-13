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

@Getter
public class WeightedNumberTest implements AbstractTester<TreeVertex> {

    private final TotalDataContainer totalDataContainer;
    private final Interpreter interpreter;
    @Setter
    private List<VariableData> variables;
    private final int weightOne;
    private final int weightTwo;
    private final int weightMistake;

    public WeightedNumberTest(
            TotalDataContainer totalDataContainer,
            Interpreter interpreter,
            int weightOne,
            int weightTwo,
            int weightMistake
    ) {
        this.totalDataContainer = totalDataContainer;
        this.interpreter = interpreter;
        this.weightOne = weightOne;
        this.weightTwo = weightTwo;
        this.weightMistake = weightMistake;
    }

    @Override
    public double test(TreeVertex vert) throws IOException {
        int fit = 0;
        int general = 0;

        for (DataLineContainer info : totalDataContainer.getRawData()) {
            boolean correctResult = ((IntegerNumber) info.getRawData()[0]).getValue() == 1;

            info.update(this.getVariables());

            double resultDoubleValue = (double) vert.getValue().getValue();

            boolean guessResult = resultDoubleValue > 0;

            if (guessResult == correctResult) {
                if (guessResult) {
                    fit += this.weightOne;
                    general += this.weightOne;
                } else {
                    fit += this.weightTwo;
                    general += this.weightTwo;
                }
            } else {
                general += this.weightMistake;
            }
        }
        return (double)fit / (double) general;
    }
}
