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
import pl.publicprojects.predictor.model.data.DataContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class RegularPoolModel extends AbstractModel {

    private record SimpleResultContainer(byte[] bytes, double result, TreeVertex graph) {
        private SimpleResultContainer(byte[] bytes, double result, TreeVertex graph) {
            this.bytes = bytes;
            this.result = result;
            this.graph = graph;
        }
    }

    private final int partAmount;
    private final Interpreter interpreter;
    private final List<DataContainer> rawData = new ArrayList<>();
    private final List<VariableData> variables = new ArrayList<>();
    private final List<SimpleResultContainer> analysedResults = new ArrayList<>();
    private ExpressGraphGenerator generator;

    @Setter
    private boolean search = true;

    public RegularPoolModel(Interpreter interpreter, int partAmount) {
        this.interpreter = interpreter;
        this.partAmount = partAmount;
    }

    public void search() throws IOException {
        int dataSize = rawData.getFirst().getSize() - 1;
        System.out.println("DATA SIZE: " + dataSize);
        this.generator = new ExpressGraphGenerator(30, 4, dataSize);

        for(int nameId = 0; nameId < dataSize; nameId++) {
            DoubleVariable variable = new DoubleVariable(nameId);
            variable.execute();
            this.variables.add(variable);
        }

        long time = System.currentTimeMillis();
        double maxResult = 0;

        int iter = 0;
        while(this.search) {
            Long res = this.timeBehaviour(generator, time, iter);
            if(res != null) time = res;

            TreeVertex vert = generator.generate(); // generate random graph
            byte[] bytes = vert.visit(); // change to bytes
            double result = this.test(bytes); // test

            this.foundRandomExpression(bytes, result, vert);
            if(maxResult < result) {
                this.analysedResults.add(new SimpleResultContainer(bytes, result, vert));
                System.out.println("Expression found " + this.analysedResults.size() + " / " + this.partAmount);
                if(this.analysedResults.size() >= this.partAmount) {
                    for(var foundRes : this.analysedResults) {
                        this.foundResult(
                                foundRes.bytes,
                                foundRes.result,
                                foundRes.graph
                        );
                        maxResult = Math.max(foundRes.result, maxResult);
                    }
                    this.analysedResults.clear();
                }
            }
            iter++;

        }
    }

    public double test(byte[] bytes) throws IOException {
        int fit = 0;
        int general = 0;

        boolean isCorrect = true;
        for(DataContainer info : this.rawData) {
            boolean correctResult = ((IntegerNumber)info.get(0)).getValue() == 1;

            info.update(this.getVariables());

            double resultDoubleValue = (double) interpreter.getAlgebraicExpressionManager()
                    .getResult(bytes)
                    .getValue();

            if (Double.isInfinite(resultDoubleValue) || Double.isNaN(resultDoubleValue)) {
                isCorrect = false;
                resultDoubleValue = 1;
            }

            boolean guessResult = resultDoubleValue > 0;

            if(guessResult == correctResult) {
                if(guessResult) {
                    int rewardForOne = 1;
                    fit += rewardForOne;
                    general += rewardForOne;
                } else {
                    int rewardForZero = 1;
                    fit += rewardForZero;
                    general += rewardForZero;//rewardForZero;
                }
            } else {
                general += 1;
            }
        }

        return (double)fit / (double) general;
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