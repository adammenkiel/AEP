package pl.publicprojects.aep.tests.graph;

import org.junit.jupiter.api.Test;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GraphGeneratorTest {

    @Test
    public void graphGeneratorTest() throws IOException {
        ExpressGraphGenerator generator = new ExpressGraphGenerator();

        Interpreter i = new Interpreter();

        DoubleVariable var = new DoubleVariable(i,0);
        var.setValue(new DoubleNumber(2));
        var.execute();

        for(int a = 0; a < 10000; a++) {
            TreeVertex vert = generator.generate();
            byte[] bytes = vert.visit();
            assertNotNull(bytes);

            Object resultValue = i.getAlgebraicExpressionManager()
                    .getResult(bytes)
                    .getValue();

            assertNotNull(resultValue);
            assertInstanceOf(Number.class, resultValue);
        }
    }
}
