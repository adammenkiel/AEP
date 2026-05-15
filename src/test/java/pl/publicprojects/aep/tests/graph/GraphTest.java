package pl.publicprojects.aep.tests.graph;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.IntegerVariable;
import pl.publicprojects.predictor.graph.expression.algebra.AlgebraicVertex;
import pl.publicprojects.predictor.graph.expression.algebra.NumberVertex;
import pl.publicprojects.predictor.graph.expression.algebra.VariableVertex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphTest {
    public static Stream<Arguments> variablesData() {
        Random random = new Random(10200);
        List<Arguments> argumentsList = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            argumentsList.add(
                    Arguments.of(
                            random.nextInt(100),
                            random.nextInt(100),
                            random.nextInt(100)
                    )
            );
        }
        return argumentsList.stream();
    }


    @ParameterizedTest
    @MethodSource("variablesData")
    public void interpreterGraphTest(int variableValue, int constFirstValue, int constSecondValue) throws IOException {
        Interpreter i = new Interpreter();

        //set variable $0$ = 100
        int value = 100;

        //nameId = 0 => variable: $0$ - names of variables are numbers
        IntegerVariable var = new IntegerVariable(i, 0);
        var.setValue(new IntegerNumber(value));
        var.execute();

        //Integers for set
        int firstNum = 2;
        int secondNum = 6;

        //build expression graph ($0$ + (firstNum * secondNum))
        int resultValue = value + (firstNum * secondNum);
        VariableVertex vert1 = new VariableVertex(var);
        NumberVertex vert2 = new NumberVertex(new IntegerNumber(firstNum));
        NumberVertex vert3 = new NumberVertex(new IntegerNumber(secondNum));

        // multiple vert2 * vert3
        AlgebraicVertex alg1 = new AlgebraicVertex(3);
        alg1.getChildren()[0] = vert2;
        alg1.getChildren()[1] = vert3;

        // add vert1 + alg1(= vert2 * vert3)
        AlgebraicVertex alg2 = new AlgebraicVertex(1);
        alg2.getChildren()[0] = vert1;
        alg2.getChildren()[1] = alg1;

        // parse expression graph into bytes[]
        byte[] bytes = alg2.visit();

        //get result of expression by interpreter
        var interpreterResult = i.getAlgebraicExpressionManager()
                .getResult(bytes)
                .getValue();


        //get result of expression by easier way (without parsing)
        var result = alg2.getValue(i).getValue();

        //Tests
        assertEquals(resultValue, result);
        assertEquals(resultValue, interpreterResult);
    }
}
