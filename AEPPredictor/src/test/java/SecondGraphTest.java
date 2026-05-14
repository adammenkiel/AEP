import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;

import java.io.IOException;

public class SecondGraphTest {
    public static void main(String[] args) throws IOException {
        ExpressGraphGenerator generator = new ExpressGraphGenerator();

        Interpreter i = new Interpreter();

        DoubleVariable var = new DoubleVariable(i,0);
        var.setValue(new DoubleNumber(2));
        var.execute();

        for(int a = 0; a < 10000; a++) {
            var.setValue(new DoubleNumber(1));
            System.out.println(" ");
            TreeVertex vert = generator.generate();
            System.out.println(vert);
            byte[] bytes = vert.visit();
            double resultValue = (double) i.getAlgebraicExpressionManager()
                    .getResult(bytes)
                    .getValue();
            System.out.println("Result: " + resultValue);
            var.setValue(new DoubleNumber(2));
            resultValue = (double) i.getAlgebraicExpressionManager()
                    .getResult(bytes)
                    .getValue();

            System.out.println("Result: " + resultValue);
            System.out.println(" ");
        }
    }
}
