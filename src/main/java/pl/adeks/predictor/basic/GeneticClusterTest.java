package pl.adeks.predictor.basic;

import pl.adeks.language.interpreter.Interpreter;
import pl.adeks.language.interpreter.data.math.LanguageNumber;
import pl.adeks.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.adeks.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.adeks.predictor.graph.TreeVertex;
import pl.adeks.predictor.model.data.DataContainer;
import pl.adeks.predictor.model.data.ProxyDataContainer;
import pl.adeks.predictor.model.models.ExpressionStandardModel;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class GeneticClusterTest {

    public static String DEFAULT_SIMPLE_TEST_FILE = "datasets/wynik.txt";


    public static void main(String[] args) throws Exception {

        Interpreter interpreter = new Interpreter();
        ProxyDataContainer container = new ProxyDataContainer(interpreter);
        ExpressionStandardModel standardModel = new ExpressionStandardModel(interpreter) {

            private double max = 0;

            @Override
            public void foundResult(byte[] bytes, double grade, TreeVertex vertex) {
                String code = vertex.toString();

                /*System.out.println("Result: " +
                        code.replace("$0$", "x")
                                .replace("$1$", "y")
                        + " grade: " + grade);*/
                try {
                    if(grade > 0.1 && grade - this.max > 0.001 ) {
                        this.max = Math.max(this.max, grade);
                        container.getExpressionList().add(vertex.visit());
                        super.getGenerator().setVariablesAmount(super.getGenerator().getVariablesAmount() + 1);
                        System.out.println("Grade: " + grade);
                        System.out.println("$" + container.getVariables().size() +"$ = " + code + "");
                    }
                } catch (Exception ignored) {}
            }

            @Override
            public void foundRandomExpression(byte[] bytes, double grade, TreeVertex vertex) {}

            @Override
            public void loadData() throws Exception {
                File file = new File(DEFAULT_SIMPLE_TEST_FILE);
                Scanner scanner = new Scanner(file); // not optimal
                while(scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");
                    LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 2];

                    double x = Double.parseDouble(lineArgs[1]) / 10;
                    double y = Double.parseDouble(lineArgs[2]) / 10;
                    numberTable[0] = new IntegerNumber(Integer.parseInt(lineArgs[0]));
                    numberTable[1] = new DoubleNumber(x);
                    numberTable[2] = new DoubleNumber(y);

                    super.getRawData().add(new DataContainer(numberTable, container));
                }
            }
        };
        container.setVariables(standardModel.getVariables());
        standardModel.loadData();
        standardModel.search();

    }
}
