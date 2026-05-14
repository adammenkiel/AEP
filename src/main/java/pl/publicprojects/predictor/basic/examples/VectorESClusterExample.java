package pl.publicprojects.predictor.basic.examples;

import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleVectorNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVectorVariable;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;
import pl.publicprojects.predictor.model.models.PoolESModel;
import pl.publicprojects.predictor.model.tester.tests.StandardVectorTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VectorESClusterExample {

    public static String DEFAULT_SIMPLE_TEST_FILE = "datasets/result.txt";

    public static void main(String[] args) throws Exception {


        Interpreter interpreter = new Interpreter();
        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        TotalDataContainer totalDataContainer = new TotalDataContainer() {
            @Override
            public List<VariableData> createVariables(int dataSize) {
                List<VariableData> list = new ArrayList<>();
                for(int nameId = 0; nameId < dataSize; nameId++) {
                    DoubleVectorVariable variable = new DoubleVectorVariable(interpreter, nameId, new ArrayList<>());
                    variable.execute();
                    list.add(variable);
                }
                return list;
            }

            @Override
            public VariableData createVariable(int nameId) throws IOException {
                DoubleVectorVariable variable = new DoubleVectorVariable(interpreter, nameId, new ArrayList<>());
                variable.execute();
                return variable;
            }

            @Override
            public LanguageNumber<?> standardize(LanguageNumber<?> var) {
                return var.plus(new DoubleVectorNumber(Nd4j.zeros(500)));
            }
        };
        PoolESModel poolESModel = new PoolESModel(
                interpreter,
                container,
                totalDataContainer,
                new StandardVectorTest(totalDataContainer, interpreter),
                new StandardVectorTest(totalDataContainer, interpreter),
                200,
                10,
                false
        ) {

            private double max = 0;
            private final Logger logger = ExpressionStandardModel.getLogger();

            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                String code = vertex.toString();
                try {
                    if(grade > 0.1 && grade - this.max > 0.001 ) {
                        this.max = Math.max(this.max, grade);
                        container.getExpressionList().add(vertex.visit());
                        super.getGenerator().setVariablesAmount(super.getGenerator().getVariablesAmount() + 1);
                        this.logger.info("Grade: {}", grade);
                        this.logger.info("${}$ = {}", container.getVariables().size(), code);
                    }
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

                List<Double> score = new ArrayList<>();
                List<Double> xVal = new ArrayList<>();
                List<Double> yVal = new ArrayList<>();
                while(scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");

                    double x = Double.parseDouble(lineArgs[1]) / 10;
                    double y = Double.parseDouble(lineArgs[2]) / 10;
                    score.add((double)Integer.parseInt(lineArgs[0]));
                    xVal.add(x);
                    yVal.add(y);
                }

                LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 2];
                numberTable[0] = new DoubleVectorNumber(score);
                numberTable[1] = new DoubleVectorNumber(xVal);
                numberTable[2] = new DoubleVectorNumber(yVal);

                super.addData(new StandardDataLineContainer(this.getTotalDataContainer(), numberTable, container));
            }
        };
        container.setVariables(poolESModel.getMainModel().getVariables());
        poolESModel.loadData();
        //poolESModel.setMainModelTreeLimit(1);
        poolESModel.search();

    }
}
