package pl.publicprojects.predictor.basic;

import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.DataContainer;
import pl.publicprojects.predictor.model.data.ProxyDataContainer;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;

import java.io.File;
import java.util.Scanner;

public class GeneticXORTest {

    public static String DEFAULT_SIMPLE_TEST_FILE = "datasets/tester.txt";

    public static void main(String[] args) throws Exception {

        Interpreter interpreter = new Interpreter();
        ProxyDataContainer container = new ProxyDataContainer(interpreter);
        ExpressionStandardModel standardModel = new ExpressionStandardModel(interpreter) {

            private double max = 0;

            @Override
            public void foundResult(byte[] bytes, double grade, TreeVertex vertex) {
                String code = vertex.toString();

                try {
                    if (grade > 0.1 && grade - this.max > 0.01) {
                        this.max = Math.max(this.max, grade);
                        container.getExpressionList().add(vertex.visit());
                        super.getGenerator().setVariablesAmount(super.getGenerator().getVariablesAmount() + 1);
                        System.out.println("Grade: " + grade);
                        System.out.println("$" + container.getVariables().size() + "$ = " + code);
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void foundRandomExpression(byte[] bytes, double grade, TreeVertex vertex) {
            }

            @Override
            public void loadData() throws Exception {
                File file = new File(DEFAULT_SIMPLE_TEST_FILE);
                Scanner scanner = new Scanner(file); // not optimal
                while (scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");
                    LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 4];

                    double a1 = Double.parseDouble(lineArgs[1]);
                    double a2 = Double.parseDouble(lineArgs[2]);
                    double a3 = Double.parseDouble(lineArgs[3]);
                    double a4 = Double.parseDouble(lineArgs[4]);

                    numberTable[0] = new IntegerNumber(Integer.parseInt(lineArgs[0]));

                    numberTable[1] = new DoubleNumber(a1);
                    numberTable[2] = new DoubleNumber(a2);
                    numberTable[3] = new DoubleNumber(a3);
                    numberTable[4] = new DoubleNumber(a4);

                    super.getRawData().add(new DataContainer(numberTable, container));
                }
            }
        };
        container.setVariables(standardModel.getVariables());
        standardModel.loadData();
        standardModel.search();

    }
}