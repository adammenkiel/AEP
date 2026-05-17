package pl.publicprojects.aep.tests.model;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;
import pl.publicprojects.aep.tests.helper.ValueContainer;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.data.container.VirtualDataLineContainer;
import pl.publicprojects.predictor.model.data.lang.DataPointer;
import pl.publicprojects.predictor.model.data.lang.VirtualVariable;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;
import pl.publicprojects.predictor.model.tester.tests.StandardNumberTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ESModelTest {

    @Test
    public void esModelSimpleTest() throws Exception {
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

        ValueContainer<Double> score = new ValueContainer<>(0.D);
        final ExpressionStandardModel model = new ExpressionStandardModel(
                interpreter,
                totalDataContainer,
                new StandardNumberTest(totalDataContainer, interpreter)
        ) {

            private double max = 0;
            private final Logger logger = ExpressionStandardModel.getLogger();

            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                score.setValue(grade);
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
                String base = System.getProperty("user.dir");
                File file = new File(base, "datasets/result.txt");

                Scanner scanner = new Scanner(file); // not optimal
                while(scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");
                    LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 2];

                    double x = Double.parseDouble(lineArgs[1]) / 10;
                    double y = Double.parseDouble(lineArgs[2]) / 10;
                    numberTable[0] = new IntegerNumber(Integer.parseInt(lineArgs[0]));
                    numberTable[1] = new DoubleNumber(x);
                    numberTable[2] = new DoubleNumber(y);

                    super.getRawData().add(new VirtualDataLineContainer(interpreter, numberTable, container, pointer));
                }
            }

            @Override
            public Long timeBehaviour(ExpressGraphGenerator generator, long time, int iter) {
                if(iter > 100000) this.setSearch(false);
                return null;
            }
        };
        container.setVariables(model.getVariables());
        model.loadData();
        model.search();
        assertTrue(score.getValue() > 0.80);
    }
}
