package pl.publicprojects.predictor.basic.examples;

import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.lang.DataPointer;
import pl.publicprojects.predictor.model.data.lang.VirtualVariable;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.data.container.VirtualDataLineContainer;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;
import pl.publicprojects.predictor.model.models.PoolESModel;
import pl.publicprojects.predictor.model.tester.tests.StandardNumberTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VirtualMakeMoonExample {

    public static String DEFAULT_SIMPLE_TEST_FILE = "C:/Users/akmen/Desktop/Modell/Fildereq/MakeMoons/output.txt";

    public static void main(String[] args) throws Exception {

        Interpreter interpreter = new Interpreter();
        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        DataPointer pointer = new DataPointer();
        TotalDataContainer totalDataContainer = new TotalDataContainer() {
            @Override
            public List<VariableData> createVariables(int dataSize) {
                List<VariableData> list = new ArrayList<>();
                for(int nameId = 0; nameId < dataSize; nameId++) {
                    VirtualVariable variable = new VirtualVariable(nameId, pointer);
                    variable.execute();
                    list.add(variable);
                }
                return list;
            }
        };
        PoolESModel poolESModel = new PoolESModel(
                interpreter,
                container,
                totalDataContainer,
                new StandardNumberTest(totalDataContainer, interpreter),
                new StandardNumberTest(totalDataContainer, interpreter),
                100,
                100,
                false
        ) {

            private double max = 0;

            @Override
            public void foundResult(byte[] bytes, double grade, TreeVertex vertex) {
                String code = vertex.toString();

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

                    super.addData(new VirtualDataLineContainer(numberTable, container, pointer));
                }
            }
        };
        container.setVariables(poolESModel.getMainModel().getVariables());
        poolESModel.loadData();
        poolESModel.setMainModelTreeLimit(5);
        poolESModel.search();

    }
}
