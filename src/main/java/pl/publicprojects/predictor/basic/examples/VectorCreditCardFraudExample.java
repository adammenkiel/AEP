package pl.publicprojects.predictor.basic.examples;

import org.nd4j.linalg.factory.Nd4j;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleVectorNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVectorVariable;
import pl.publicprojects.predictor.model.models.PoolESModel;
import pl.publicprojects.predictor.model.tester.tests.StandardVectorTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VectorCreditCardFraudExample {

    public static String DEFAULT_SIMPLE_TEST_FILE = "Please download from https://www.kaggle.com/datasets/mlg-ulb/creditcardfraud/data";

    public static void main(String[] args) throws Exception {

        Interpreter interpreter = new Interpreter();
        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        TotalDataContainer totalDataContainer = new TotalDataContainer() {
            @Override
            public List<VariableData> createVariables(int dataSize) {
                List<VariableData> list = new ArrayList<>();
                for(int nameId = 0; nameId < dataSize; nameId++) {
                    DoubleVectorVariable variable = new DoubleVectorVariable(nameId, new ArrayList<>());
                    variable.execute();
                    list.add(variable);
                }
                return list;
            }

            @Override
            public VariableData createVariable(int nameId) throws IOException {
                DoubleVectorVariable variable = new DoubleVectorVariable(nameId, new ArrayList<>());
                variable.execute();
                return variable;
            }

            @Override
            public LanguageNumber<?> standardize(LanguageNumber<?> var) {
                return var.plus(new DoubleVectorNumber(Nd4j.zeros(284807)));
            }
        };

        PoolESModel poolESModel = new PoolESModel(
                interpreter,
                container,
                totalDataContainer,
                new StandardVectorTest(totalDataContainer, interpreter),
                new StandardVectorTest(totalDataContainer, interpreter),
                0,
                20,
                false
        ) {

            private double max = 0;

            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                String code = vertex.toString();

                try {
                    if(grade > 0.1 && grade - this.max > 0.01) {
                        this.max = Math.max(this.max, grade);
                        container.getExpressionList().add(vertex.visit());
                        super.getGenerator().setVariablesAmount(super.getGenerator().getVariablesAmount() + 1);
                        System.out.println("Grade: " + grade);
                        System.out.println("$" + container.getVariables().size() +"$ = " + code);
                    }
                } catch (Exception ignored) {}
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


                /*
                    *  File file = new File(DEFAULT_SIMPLE_TEST_FILE);
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

                super.addData(new StandardDataLineContainer(this.getTotalDataContainer(), numberTable, container));*/

                //super.getTotalDataContainer().getRawData().add(new VirtualDataLineContainer(numberTable, container, pointer));



            }
        };
        container.setVariables(poolESModel.getMainModel().getVariables());
        poolESModel.setMainModelTreeLimit(2);
        poolESModel.loadData();
        poolESModel.search();

    }
}
