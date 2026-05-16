package pl.publicprojects.predictor.model.data.container;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.model.data.lang.DataPointer;
import pl.publicprojects.predictor.model.data.lang.VirtualVariable;
import pl.publicprojects.predictor.model.data.DataLineContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Optimized, not thread-safe table line that scores rawData and
 * results of expressions that be treated as new data columns
 */
@Getter
public class VirtualDataLineContainer implements DataLineContainer {

    private final Interpreter interpreter;
    private final LanguageNumber<?>[] rawData;
    private final ProxyDataLineContainer proxyDataContainer;
    private final DataPointer dataPointer;

    private final List<LanguageNumber<?>> frozenValues = new ArrayList<>();

    /**
     * @param interpreter Interpreter for eval expressions
     * @param rawData Initial data for analyse
     * @param proxyDataContainer Expressions that be treated as new data columns,
     *                          results of new expressions will be scored in frozenValue
     *                          so every expression is evaluated just one time for every VirtualDataContainer
     * @param dataPointer DataPointer points VirtualDataLineContainer that
     *                    should be used to evaluate result of considered expression
     */
    public VirtualDataLineContainer(
            Interpreter interpreter,
            LanguageNumber<?>[] rawData,
            ProxyDataLineContainer proxyDataContainer,
            DataPointer dataPointer
    ) {
        this.interpreter = interpreter;
        this.rawData = rawData;
        this.proxyDataContainer = proxyDataContainer;
        this.dataPointer = dataPointer;
    }


    /**
     * @param index Index of data we want get
     * @return Returns column of this table with fixed index
     */
    public LanguageNumber<?> get(int index) throws IOException {
        if(index < this.rawData.length) {
            return this.rawData[index];
        }
        int proxyIndex = index - this.rawData.length;
        if(proxyIndex < this.frozenValues.size()) {
            return this.frozenValues.get(proxyIndex);
        }
        this.update(proxyDataContainer.getVariables());
        return this.frozenValues.get(proxyIndex);
    }

    /**
     * @return Returns total size of container (raw data + proxied data (values of evaluated expressions))
     */
    public int getSize() {
        return this.rawData.length + this.proxyDataContainer.getExpressionList().size();
    }

    /**
     * If there is no enough variables, creates new VirtualVariable with fixed dataPointer
     * @param variables List of variables
     */
    public void update(List<VariableData> variables) throws IOException {
        this.dataPointer.setPointerContainer(this); // rawUpdate

        while(this.getSize() - 1 > variables.size()) {
            VirtualVariable variable = new VirtualVariable(this.interpreter, variables.size(), this.dataPointer);
            variable.execute();
            variables.add(variable);
        }

        while(this.proxyDataContainer.getExpressionList().size() > this.frozenValues.size()) {
            var val = this.proxyDataContainer.getValue(this.frozenValues.size());
            this.frozenValues.add(val);
        }
    }
}
