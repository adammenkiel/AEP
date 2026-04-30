package pl.adeks.predictor.model.data;

import lombok.Getter;
import lombok.Setter;
import pl.adeks.language.interpreter.Interpreter;
import pl.adeks.language.interpreter.data.math.LanguageNumber;
import pl.adeks.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.adeks.language.interpreter.data.types.VariableData;
import pl.adeks.language.interpreter.data.types.variables.numeric.DoubleVariable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ProxyDataContainer {

    private final Interpreter interpreter;
    private final List<byte[]> expressionList = new ArrayList<>();
    @Setter
    private List<VariableData> variables;

    public ProxyDataContainer(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public double function(double d) { // RElu
        return Math.max(d, 0);
    }

    public LanguageNumber<?> getValue(DataContainer container, int index) throws IOException {
        byte[] expression = expressionList.get(index);
        //var info = container.getRawData();
        //container.update(this.variables);
        /*for(int i = 0; i < info.length - 1; i++) {
            this.getVariables().get(i).setValue(info[i + 1]);
        }*/

        double resultDoubleValue = (double) interpreter.getAlgebraicExpressionManager()
                .getResult(expression)
                .getValue();

        return new DoubleNumber(resultDoubleValue);
    }

}
