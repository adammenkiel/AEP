package pl.publicprojects.predictor.basic.examples;

import org.slf4j.Logger;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;
import pl.publicprojects.predictor.model.tester.tests.StandardNumberTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GeneticDiabetesDBExample {

    public static String DEFAULT_SIMPLE_TEST_FILE = "Please download from https://www.kaggle.com/datasets/mathchi/diabetes-data-set";


    public static void main(String[] args) throws Exception {

        Interpreter interpreter = new Interpreter();
        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        TotalDataContainer totalDataContainer = new TotalDataContainer() {
            @Override
            public List<VariableData> createVariables(int dataSize) {
                List<VariableData> list = new ArrayList<>();
                for(int nameId = 0; nameId < dataSize; nameId++) {
                    DoubleVariable variable = new DoubleVariable(interpreter, nameId);
                    variable.execute();
                    list.add(variable);
                }
                return list;
            }

            @Override
            public VariableData createVariable(int nameId) throws IOException {
                DoubleVariable variable = new DoubleVariable(interpreter, nameId);
                variable.execute();
                variable.setValue(new DoubleNumber(0));
                return variable;
            }

            @Override
            public LanguageNumber<?> standardize(LanguageNumber<?> var) {
                return var.plus(new DoubleNumber(0));
            }
        };
        ExpressionStandardModel standardModel = new ExpressionStandardModel(
                interpreter,
                totalDataContainer,
                new StandardNumberTest(totalDataContainer, interpreter)
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
                while(scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");
                    LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 8];

                    double a1 = Double.parseDouble(lineArgs[1]);
                    double a2 = Double.parseDouble(lineArgs[2]);
                    double a3 = Double.parseDouble(lineArgs[3]);
                    double a4 = Double.parseDouble(lineArgs[4]);
                    double a5 = Double.parseDouble(lineArgs[5]);
                    double a6 = Double.parseDouble(lineArgs[6]);
                    double a7 = Double.parseDouble(lineArgs[7]);
                    double a8 = Double.parseDouble(lineArgs[8]);

                    numberTable[0] = new IntegerNumber(Integer.parseInt(lineArgs[0]));

                    numberTable[1] = new DoubleNumber(a1);
                    numberTable[2] = new DoubleNumber(a2);
                    numberTable[3] = new DoubleNumber(a3);
                    numberTable[4] = new DoubleNumber(a4);
                    numberTable[5] = new DoubleNumber(a5);
                    numberTable[6] = new DoubleNumber(a6);
                    numberTable[7] = new DoubleNumber(a7);
                    numberTable[8] = new DoubleNumber(a8);

                    super.getTotalDataContainer().getRawData().add(new StandardDataLineContainer(super.getTotalDataContainer(), numberTable, container));
                }
            }
        };
        container.setVariables(standardModel.getVariables());
        standardModel.loadData();
        standardModel.search();

    }
}