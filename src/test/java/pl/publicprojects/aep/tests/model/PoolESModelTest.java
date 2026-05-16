package pl.publicprojects.aep.tests.model;

import org.junit.jupiter.api.Test;
import org.nd4j.common.primitives.AtomicDouble;
import org.slf4j.Logger;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.basic.examples.VirtualPoolESClusterExample;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.data.container.VirtualDataLineContainer;
import pl.publicprojects.predictor.model.data.lang.DataPointer;
import pl.publicprojects.predictor.model.data.lang.VirtualVariable;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;
import pl.publicprojects.predictor.model.models.PoolESModel;
import pl.publicprojects.predictor.model.tester.tests.StandardNumberTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * That model able to find a good result (< 90%) of
 * dataset/result.txt problem after 100000 iterations.
 */
public class PoolESModelTest {

    @Test
    public void poolESModelTest() throws Exception {
        AtomicDouble score = new AtomicDouble(0);

        Interpreter interpreter = new Interpreter();
        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        DataPointer pointer = new DataPointer();
        TotalDataContainer totalDataContainer = new TotalDataContainer() {
            @Override
            public List<VariableData> createVariables(int dataSize) {
                List<VariableData> list = new ArrayList<>();
                for(int nameId = 0; nameId < dataSize; nameId++) {
                    VirtualVariable variable = new VirtualVariable(interpreter, nameId, pointer);
                    variable.execute();
                    list.add(variable);
                }
                return list;
            }

            @Override
            public VariableData createVariable(int nameId) {
                VirtualVariable variable = new VirtualVariable(interpreter, nameId, pointer);
                variable.execute();
                return variable;
            }

            @Override
            public LanguageNumber<?> standardize(LanguageNumber<?> var) {
                return var.plus(new DoubleNumber(0));
            }
        };
        PoolESModel poolESModel = new PoolESModel(
                interpreter,
                container,
                totalDataContainer,
                new StandardNumberTest(totalDataContainer, interpreter),
                new StandardNumberTest(totalDataContainer, interpreter),
                100,
                10,
                false
        ) {

            private int iterations = 0;

            private double max = 0;
            private final Logger logger = ExpressionStandardModel.getLogger();

            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                score.set(grade);
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
            public void foundRandomExpression(double grade, TreeVertex vertex) {
                if(iterations > 100000)  {
                    this.setSearch(false);
                }
                iterations++;
            }

            @Override
            public void loadData() throws Exception {
                File file = new File("datasets/result.txt");

                Scanner scanner = new Scanner(file); // not optimal
                while(scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");
                    LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 2];

                    numberTable[0] = new IntegerNumber(Integer.parseInt(lineArgs[0]));
                    numberTable[1] = new DoubleNumber(Double.parseDouble(lineArgs[1]) / 10);
                    numberTable[2] = new DoubleNumber(Double.parseDouble(lineArgs[2]) / 10);

                    super.addData(new VirtualDataLineContainer(interpreter, numberTable, container, pointer));
                }
            }
        };
        container.setVariables(poolESModel.getMainModel().getVariables());
        poolESModel.loadData();
        poolESModel.setMainModelTreeLimit(5);
        poolESModel.search();
        assertTrue(score.get() > 0.90);
    }
}
