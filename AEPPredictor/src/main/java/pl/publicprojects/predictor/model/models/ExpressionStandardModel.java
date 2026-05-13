package pl.publicprojects.predictor.model.models;

import lombok.Getter;
import lombok.Setter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;
import pl.publicprojects.predictor.model.AbstractModel;
import pl.publicprojects.predictor.model.data.DataLineContainer;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.tester.AbstractTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ExpressionStandardModel implements AbstractModel {

    private final Interpreter interpreter;
    private final AbstractTester<TreeVertex> tester;
    private final TotalDataContainer totalDataContainer;
    private final List<DataLineContainer> rawData;
    private final List<VariableData> variables = new ArrayList<>();
    private ExpressGraphGenerator generator;
    private boolean haveTreeLimits = false;
    private int pointLimit = 10;

    @Setter
    private boolean search = true;

    public ExpressionStandardModel(Interpreter interpreter, TotalDataContainer totalDataContainer, AbstractTester<TreeVertex> tester) {
        this.totalDataContainer = totalDataContainer;
        this.rawData = this.totalDataContainer.getRawData();
        this.interpreter = interpreter;
        this.tester = tester;
        this.tester.setVariables(this.variables);
    }

    public void setTreeLimit(int pointLimit) {
        this.pointLimit = pointLimit;
        this.haveTreeLimits = true;
    }

    public void search() throws IOException {
        int dataSize = totalDataContainer.getRawData().getFirst().getSize() - 1;
        System.out.println("DATA SIZE: " + dataSize);
        this.generator = new ExpressGraphGenerator(30, 4, dataSize);

        if(this.haveTreeLimits) {
            this.generator.setHaveLimit(true);
            this.generator.setPointLimit(this.pointLimit);
            this.generator.setVertexEndChance(50);
        }

        this.variables.addAll(this.getTotalDataContainer().createVariables(dataSize));

        long time = System.currentTimeMillis();
        double maxResult = 0;

        int iter = 0;
        while(this.search) {
            final Long res = this.timeBehaviour(generator, time, iter);
            if(res != null) time = res;

            final TreeVertex vert = generator.generate(); // generate random graph

            final double result = this.tester.test(vert); // test
            //byte[] bytes = vert.visit(); // change to bytes

            final byte[] bytes = new byte[0];
            this.foundRandomExpression(bytes, result, vert);
            if(maxResult < result) {
                this.foundResult(bytes, result, vert);
                maxResult = result;
            }
            iter++;

        }
    }

    @Deprecated
    public double test(byte[] bytes) throws IOException {
        throw new RuntimeException("Unsupported!");
    }

    public abstract void foundResult(byte[] bytes, double grade, TreeVertex vertex);

    public abstract void foundRandomExpression(byte[] bytes, double grade, TreeVertex vertex);


    public Long timeBehaviour(ExpressGraphGenerator generator, long time, int iter) {
        double timeChange = System.currentTimeMillis() - time;
        if(timeChange > 30000) { // that's simple controller
            if(generator.getVertexChance() < 36) {
                generator.setVertexChance(generator.getVertexChance() + 1);
            }
            System.out.println("Thinking... (Iteration: " + iter + ")");
            return System.currentTimeMillis();
        }
        return null;
    }
}