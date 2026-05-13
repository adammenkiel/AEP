package pl.publicprojects.predictor.model.data.container;

import lombok.Getter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.model.data.DataLineContainer;
import pl.publicprojects.predictor.model.data.TotalDataContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* Class for store real data from datasets called
* rawData and proxyData that are results of generated expressions
* */
@Getter
public class StandardDataLineContainer implements DataLineContainer {

    private TotalDataContainer totalDataContainer;
    private final LanguageNumber<?>[] rawData;
    private final ProxyDataLineContainer proxyDataContainer;

    private boolean freezeValues = true;
    private final List<LanguageNumber<?>> frozenValues = new ArrayList<>();

    /**
    * @param rawData Standard dataSet that we load at the start
    * @param proxyDataContainer While model running, we can add new, good-scored expression for generating better results
    */
    public StandardDataLineContainer(TotalDataContainer totalDataContainer, LanguageNumber<?>[] rawData, ProxyDataLineContainer proxyDataContainer) {
        this.totalDataContainer = totalDataContainer;
        this.rawData = rawData;
        this.proxyDataContainer = proxyDataContainer;
    }

    /**
    * @param rawData Standard dataSet that we load at the start
    * @param proxyDataContainer While model running, we can add new, good-scored expression for generating better results
    * @param freezeValues If freezeValues=true you can't update old proxyValues by very easy way
    */
    public StandardDataLineContainer(
            LanguageNumber<?>[] rawData,
            ProxyDataLineContainer proxyDataContainer,
            boolean freezeValues
    ) {
        this.rawData = rawData;
        this.proxyDataContainer = proxyDataContainer;
        this.freezeValues = freezeValues;
    }

    /**
    * Function for get data with fixed index.
    * @param index that you data want to load
    */
    public LanguageNumber<?> get(int index) throws IOException {
        if(index < this.rawData.length) {
            return this.rawData[index];
        }
        int proxyIndex = index - this.rawData.length;
        this.update(proxyDataContainer.getVariables());
        return this.proxyDataContainer.getValue(proxyIndex);
    }

    /**
    * Total size (with proxied data)
    */
    public int getSize() {
        return this.rawData.length + this.proxyDataContainer.getExpressionList().size();
    }

    /**
    * I could make it more optimal yet
    * When I have a freeze mode I can load only variables that I need to use
    * or create something like proxy variable
    */
    public void update(List<VariableData> variables) throws IOException {
        this.rawUpdate(variables);
        while(this.getSize() - 1 > variables.size()) {
            VariableData variable = this.totalDataContainer.createVariable(variables.size());
            variable.execute();
            variables.add(variable);

            /*new DoubleVariable(variables.size());
            variable.execute();
            variable.setValue(new DoubleNumber(0));
            variables.add(variable);*/
        }

        if(!freezeValues) {
            for(int i = rawData.length - 1; i < this.getSize() - 1; i++) {
                int proxyIndex = i - this.rawData.length + 1;
                variables.get(i).setValue(this.totalDataContainer.standardize(this.proxyDataContainer.getValue(proxyIndex)));
            }
        } else {
            while(this.proxyDataContainer.getExpressionList().size() > this.frozenValues.size()) {
                var val = this.proxyDataContainer.getValue(this.frozenValues.size());
                this.frozenValues.add(this.totalDataContainer.standardize(val));
            }

            for(int i = rawData.length - 1; i < this.getSize() - 1; i++) {
                int proxyIndex = i - this.rawData.length + 1;
                variables.get(i).setValue(this.frozenValues.get(proxyIndex));
            }
        }
    }

    /**
    * Function for set values of variables into rawData values (just these variables responsible for rawData)
    * @param variables variables list
    */
    public void rawUpdate(List<VariableData> variables) throws IOException {
        for(int i = 0; i < rawData.length - 1; i++) {
            variables.get(i).setValue(rawData[i + 1]);
        }
    }
}
