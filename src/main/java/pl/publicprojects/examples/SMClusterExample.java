package pl.publicprojects.examples;

import org.slf4j.Logger;
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
import pl.publicprojects.predictor.model.models.StandardModel;
import pl.publicprojects.predictor.model.tester.tests.StandardNumberTest;

import java.io.File;
import java.util.Scanner;

public class SMClusterExample {

    public static void main(String[] args) throws Exception {

        Interpreter interpreter = new Interpreter();
        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        TotalDataContainer totalDataContainer = new DoubleTotalDataContainer(interpreter);

        StandardModel standardModel = new StandardModel(
                interpreter,
                totalDataContainer,
                new StandardNumberTest(totalDataContainer, interpreter)
        ) {

            private final Logger logger = ExpressionStandardModel.getLogger();

            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                String code = vertex.toString();
                this.logger.info("Found expression: {} grade: {}", code, grade);
            }

            @Override
            public void foundRandomExpression(double grade, TreeVertex vertex) {}

            @Override
            public void loadData() throws Exception {
                File file = new File("datasets/result.txt");
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");
                    LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 2];

                    numberTable[0] = new IntegerNumber(Integer.parseInt(lineArgs[0]));

                    for(int i = 1; i <= 2; i++)
                        numberTable[i] = new DoubleNumber(Double.parseDouble(lineArgs[i]) / 10);

                    super.getTotalDataContainer().getRawData().add(
                            new StandardDataLineContainer(super.getTotalDataContainer(), numberTable, container)
                    );
                }
            }
        };
        container.setVariables(standardModel.getVariables());
        standardModel.loadData();
        standardModel.search();

    }
}
