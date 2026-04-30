import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.IntegerVariable;
import pl.publicprojects.predictor.graph.expression.algebra.AlgebraicVertex;
import pl.publicprojects.predictor.graph.expression.algebra.NumberVertex;
import pl.publicprojects.predictor.graph.expression.algebra.VariableVertex;

import java.io.IOException;

public class GraphTest {
    public static String debugTable(byte[] bytes) {
        StringBuilder lol = new StringBuilder("{");
        for(byte b : bytes) {
            lol.append((int) b).append(", ");
        }
        lol.append("}");
        return lol.toString();
    }


    public static void main(String[] args) throws IOException {
        Interpreter i = new Interpreter();

        IntegerVariable var = new IntegerVariable(0);
        var.setValue(new IntegerNumber(100));
        var.execute();

        VariableVertex vert1 = new VariableVertex(var);
        NumberVertex vert2 = new NumberVertex(new IntegerNumber(2));
        NumberVertex vert3 = new NumberVertex(new IntegerNumber(6));
        AlgebraicVertex alg1 = new AlgebraicVertex(3);
        alg1.getChildren()[0] = vert2;
        alg1.getChildren()[1] = vert3;
        AlgebraicVertex alg2 = new AlgebraicVertex(1);
        alg2.getChildren()[0] = vert1;
        alg2.getChildren()[1] = alg1;


        byte[] bytes = alg2.visit();
        System.out.println("Expression: " + alg2);
        System.out.println("Debug table: " + debugTable(bytes));
        System.out.println("Result: " +
                i.getAlgebraicExpressionManager()
                        .getResult(bytes)
                        .getValue()
        );

    }
}
