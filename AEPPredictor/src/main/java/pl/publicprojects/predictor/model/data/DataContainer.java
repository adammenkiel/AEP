package pl.publicprojects.predictor.model.data;

import lombok.Getter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DataContainer {
    private final LanguageNumber<?>[] rawData;
    private final ProxyDataContainer proxyDataContainer;

    private boolean freezeValues = true;
    private final List<LanguageNumber<?>> frozenValues = new ArrayList<>();

    public DataContainer(LanguageNumber<?>[] rawData, ProxyDataContainer proxyDataContainer) {
        this.rawData = rawData;
        this.proxyDataContainer = proxyDataContainer;
    }
    public DataContainer(LanguageNumber<?>[] rawData, ProxyDataContainer proxyDataContainer, boolean freezeValues) {
        this.rawData = rawData;
        this.proxyDataContainer = proxyDataContainer;
        this.freezeValues = freezeValues;
    }

    public LanguageNumber<?> get(int index) throws IOException {
        if(index < this.rawData.length) {
            return this.rawData[index];
        }
        int proxyIndex = index - this.rawData.length;
        this.update(proxyDataContainer.getVariables());
        return this.proxyDataContainer.getValue(this, proxyIndex);
    }


    public int getSize() {
        return this.rawData.length + this.proxyDataContainer.getExpressionList().size();
    }


    public void update(List<VariableData> variables) throws IOException {
        this.rawUpdate(variables);
        while(this.getSize() - 1 > variables.size()) {
            //System.out.println("Set variable: " + variables.size());
            DoubleVariable variable = new DoubleVariable(variables.size());
            variable.execute();
            variable.setValue(new DoubleNumber(0));
            variables.add(variable);
        }
        //System.out.println(rawData.length + " " + this.getSize());
        if(!freezeValues) {
            for(int i = rawData.length - 1; i < this.getSize() - 1; i++) {
                int proxyIndex = i - this.rawData.length + 1;
                variables.get(i).setValue(this.proxyDataContainer.getValue(this, proxyIndex));
            }
        } else {
            while(this.proxyDataContainer.getExpressionList().size() > this.frozenValues.size()) {
                var val = this.proxyDataContainer.getValue(this, this.frozenValues.size());
                this.frozenValues.add(val);
            }

            for(int i = rawData.length - 1; i < this.getSize() - 1; i++) {
                int proxyIndex = i - this.rawData.length + 1;
                variables.get(i).setValue(this.frozenValues.get(proxyIndex));
            }
        }
        /*
        for(int i = 0; i < this.getSize() - 1; i++) {
            System.out.println("val " + i + ": " + variables.get(i).getValue());
        }
        System.out.println(" ");*/
    }
    public void rawUpdate(List<VariableData> variables) throws IOException {
        for(int i = 0; i < rawData.length - 1; i++) {
            //System.out.println("Set variable: " + i);
            //System.out.println("change " + i + " " + variables.size());
            variables.get(i).setValue(rawData[i + 1]);
        }
    }
}
