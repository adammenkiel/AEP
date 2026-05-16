package pl.publicprojects.predictor.model.tester;

import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.graph.TreeVertex;

import java.io.IOException;
import java.util.List;

/**
 * Interface for implement various test methods of expression
 * @param <T> Expression in scored in <T> form for test
 */
public interface AbstractTester<T> {

    /**
     * Method for test expressions
     *
     * @param expr Expression in fixed form
     * @return Score of quality of this expression
     */
    double test(T expr) throws IOException;

    /**
     * Scores variables for DataLineContainer#update
     *
     * @param variables Variable list
     */
    void setVariables(List<VariableData> variables);
}
