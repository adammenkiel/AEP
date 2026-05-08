package pl.publicprojects.predictor.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class TotalDataContainer {
    private final List<DataLineContainer> rawData = new ArrayList<>();

    public abstract List<VariableData> createVariables(int dataSize);
}
