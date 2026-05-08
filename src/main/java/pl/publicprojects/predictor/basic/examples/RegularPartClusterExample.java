package pl.publicprojects.predictor.basic.examples;


import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.models.RegularPoolModel;


import java.io.File;
import java.util.Scanner;

public class RegularPartClusterExample {

    public static String DEFAULT_SIMPLE_TEST_FILE = "datasets/result.txt";

    public static void main(String[] args) throws Exception {

        System.out.println("Running...");
        Interpreter interpreter = new Interpreter();
        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        RegularPoolModel regularModel = new RegularPoolModel(interpreter, 30) {

            @Override
            public void foundResult(byte[] bytes, double grade, TreeVertex vertex) {
                System.out.println("Founded...");
                String code = vertex.toString();

                try {
                    container.getExpressionList().add(vertex.visit());
                    super.getGenerator().setVariablesAmount(super.getGenerator().getVariablesAmount() + 1);
                    System.out.println("Grade: " + grade);
                    System.out.println("$" + (this.getRawData().getFirst().getRawData().length + container.getExpressionList().size() - 2) +"$ = " + code + "");

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

                    super.getRawData().add(new StandardDataLineContainer(numberTable, container));
                }
            }
        };
        container.setVariables(regularModel.getVariables());
        regularModel.loadData();
        regularModel.search();

    }
}
