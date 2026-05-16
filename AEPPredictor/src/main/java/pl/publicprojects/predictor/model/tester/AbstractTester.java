package pl.publicprojects.predictor.model.tester;

import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.graph.TreeVertex;

import java.io.IOException;
import java.util.List;

public interface AbstractTester<T> {

    double test(T expr) throws IOException;
    void setVariables(List<VariableData> variables);
}
