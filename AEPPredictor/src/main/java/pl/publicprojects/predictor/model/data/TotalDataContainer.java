package pl.publicprojects.predictor.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class TotalDataContainer {
    /**
     * Data with all lines of considered table
     */
    private final List<DataLineContainer> rawData = new ArrayList<>();

    /**
     * Method for creating start variables,
     * as there is many types of VariableData (VirtualVariable too),
     * we require to create data with correct type
     *
     * @param dataSize Size of initial data columns
     * @return List of variables
     */
    public abstract List<VariableData> createVariables(int dataSize);

    /**
     * Method for create a single variable.
     *
     * @param nameId nameId of new variable
     * @return Returns result variableData
     */
    public abstract VariableData createVariable(int nameId) throws IOException;

    /**
     * Method for parse data into specific, result type,
     * that's useful especially at the end when it's required to have vector
     * or number with specific type for make operations with it and grade
     *
     * @param var Language number with arbitrary type
     * @return Returns Language number with fixed type
     */
    public abstract LanguageNumber<?> standardize(LanguageNumber<?> var) throws IOException;
}
