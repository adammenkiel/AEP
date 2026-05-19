package pl.publicprojects.examples;

import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleVectorNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVectorVariable;
import pl.publicprojects.predictor.model.data.container.total.DoubleVectorTotalDataContainer;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;
import pl.publicprojects.predictor.model.models.PoolESModel;
import pl.publicprojects.predictor.model.tester.tests.StandardVectorTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VectorCreditCardFraudExample {

    public static String DEFAULT_SIMPLE_TEST_FILE = "C:/Users/akmen/Desktop/Diabetes/Frauds/creditcard_processed.txt";

    public static void main(String[] args) throws Exception {

        Interpreter interpreter = new Interpreter();
        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        TotalDataContainer totalDataContainer = new DoubleVectorTotalDataContainer(interpreter, 284807);

        /*
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
                return var.plus(new DoubleVectorNumber(Nd4j.zeros(284807)));
            }
        };*/

        PoolESModel poolESModel = new PoolESModel(
                interpreter,
                container,
                totalDataContainer,
                new StandardVectorTest(totalDataContainer, interpreter),
                new StandardVectorTest(totalDataContainer, interpreter),
                0,
                200,
                false
        ) {

            private double max = 0;
            private final Logger logger = ExpressionStandardModel.getLogger();

            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                String code = vertex.toString();

                try {
                    if(grade > 0.1 && grade - this.max > 0.01) {
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

                ArrayList<Double>[] tables = new ArrayList[1 + 30];
                for(int i = 0; i < tables.length; i++) tables[i] = new ArrayList<>();

                while(scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");
                    for(int i = 0; i <= 30; i++) tables[i].add(Double.parseDouble(lineArgs[i]));
                }

                LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 30];
                int i = 0;
                for(ArrayList<Double> arr : tables) {
                    numberTable[i] = new DoubleVectorNumber(arr);
                    i++;
                }
                super.addData(new StandardDataLineContainer(this.getTotalDataContainer(), numberTable, container));

            }
        };
        container.setVariables(poolESModel.getMainModel().getVariables());
        poolESModel.setMainModelTreeLimit(2);
        poolESModel.loadData();
        poolESModel.search();

    }
}
