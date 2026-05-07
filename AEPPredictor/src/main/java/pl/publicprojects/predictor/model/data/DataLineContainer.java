package pl.publicprojects.predictor.model.data;

import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;

import java.io.IOException;
import java.util.List;

public interface DataLineContainer {
    int getSize();
    LanguageNumber<?> get(int index) throws IOException;
    void update(List<VariableData> variables) throws IOException;
    LanguageNumber<?>[] getRawData();
}
