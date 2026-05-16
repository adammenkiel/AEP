package pl.publicprojects.predictor.model.data;

import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;

import java.io.IOException;
import java.util.List;

/**
 * That interface allows to score and process data by various ways
 */
public interface DataLineContainer {
    /**
     * @return Returns size of data
     */
    int getSize();

    /**
     * @param index Index of data we require.
     * @return Data of fixed index
     */
    LanguageNumber<?> get(int index) throws IOException;

    /**
     * Set used variables to values of this data
     * @param variables Variables list
     */
    void update(List<VariableData> variables) throws IOException;

    /**
     * @return Returns initial data
     */
    LanguageNumber<?>[] getRawData();
}
