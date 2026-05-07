package pl.publicprojects.predictor.model.data.container;

import lombok.Getter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.model.data.lang.DataPointer;
import pl.publicprojects.predictor.model.data.lang.VirtualVariable;
import pl.publicprojects.predictor.model.data.DataLineContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class VirtualDataLineContainer implements DataLineContainer {
    private final LanguageNumber<?>[] rawData;
    private final ProxyDataLineContainer proxyDataContainer;
    private final DataPointer dataPointer;

    private final List<LanguageNumber<?>> frozenValues = new ArrayList<>();

    public VirtualDataLineContainer(LanguageNumber<?>[] rawData, ProxyDataLineContainer proxyDataContainer, DataPointer dataPointer) {
        this.rawData = rawData;
        this.proxyDataContainer = proxyDataContainer;
        this.dataPointer = dataPointer;
    }

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

    public int getSize() {
        return this.rawData.length + this.proxyDataContainer.getExpressionList().size();
    }

    public void update(List<VariableData> variables) throws IOException {
        this.dataPointer.setPointerContainer(this); // rawUpdate

        while(this.getSize() - 1 > variables.size()) {
            VirtualVariable variable = new VirtualVariable(variables.size(), this.dataPointer);
            variable.execute();
            variables.add(variable);
        }

        while(this.proxyDataContainer.getExpressionList().size() > this.frozenValues.size()) {
            var val = this.proxyDataContainer.getValue(this.frozenValues.size());
            this.frozenValues.add(val);
        }
    }
}
