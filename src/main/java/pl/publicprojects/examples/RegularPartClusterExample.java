package pl.publicprojects.examples;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.data.container.total.DoubleTotalDataContainer;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;
import pl.publicprojects.predictor.model.models.RegularPoolModel;
import pl.publicprojects.predictor.model.tester.tests.StandardNumberTest;


import java.io.File;
import java.util.Scanner;

public class RegularPartClusterExample {

    public static String DEFAULT_SIMPLE_TEST_FILE = "datasets/result.txt";
    private static final Logger logger = LoggerFactory.getLogger(RegularPartClusterExample.class);

    public static void main(String[] args) throws Exception {

        logger.info("Running...");
        Interpreter interpreter = new Interpreter();
        TotalDataContainer totalDataContainer = new DoubleTotalDataContainer(interpreter);

        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        RegularPoolModel regularModel = new RegularPoolModel(
                interpreter,
                totalDataContainer,
                new StandardNumberTest(totalDataContainer, interpreter),
                30
        ) {

            private final Logger logger = ExpressionStandardModel.getLogger();

            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                this.logger.info("Founded...");
                String code = vertex.toString();
                try {
                    container.getExpressionList().add(vertex.visit());
                    super.getGenerator().setVariablesAmount(super.getGenerator().getVariablesAmount() + 1);
                    this.logger.info("Grade: {}", grade);
                    this.logger.info("${}$ = {}", this.getRawData().getFirst().getRawData().length + container.getExpressionList().size() - 2, code);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void foundRandomExpression(double grade, TreeVertex vertex) {}

            @Override
            public void loadData() throws Exception {
                File file = new File(DEFAULT_SIMPLE_TEST_FILE);
                Scanner scanner = new Scanner(file); // not optimal
                while(scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");
                    LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 2];

                    numberTable[0] = new IntegerNumber(Integer.parseInt(lineArgs[0]));

                    for(int i = 1; i <= 2; i++)
                        numberTable[i] = new DoubleNumber(Double.parseDouble(lineArgs[i]) / 10);

                    super.getRawData().add(new StandardDataLineContainer(totalDataContainer, numberTable, container));
                }
            }
        };
        container.setVariables(regularModel.getVariables());
        regularModel.loadData();
        regularModel.search();

    }
}
