package pl.publicprojects.predictor.model.models;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;
import pl.publicprojects.predictor.model.AbstractModel;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.predictor.model.tester.AbstractTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class RegularPoolModel implements AbstractModel {

    private record SimpleResultContainer(double result, TreeVertex graph) {
        private SimpleResultContainer(double result, TreeVertex graph) {
            this.result = result;
            this.graph = graph;
        }
    }

    private final int partAmount;
    private final Interpreter interpreter;
    private final AbstractTester<TreeVertex> tester;
    private final List<StandardDataLineContainer> rawData = new ArrayList<>();
    private final List<VariableData> variables = new ArrayList<>();
    private final List<SimpleResultContainer> analysedResults = new ArrayList<>();
    private final TotalDataContainer totalDataContainer;
    private boolean haveTreeLimits = false;
    private int pointLimit = 10;
    private ExpressGraphGenerator generator;

    @Getter
    private static final Logger logger = LoggerFactory.getLogger(PoolESVecModel.class);

    @Setter
    private boolean search = true;

    public RegularPoolModel(Interpreter interpreter, TotalDataContainer totalDataContainer, AbstractTester<TreeVertex> tester, int partAmount) {
        this.interpreter = interpreter;
        this.totalDataContainer = totalDataContainer;
        this.partAmount = partAmount;
        this.tester = tester;
        this.tester.setVariables(variables);
    }

    public void search() throws IOException {
        int dataSize = rawData.getFirst().getSize() - 1;
        logger.info("DATA SIZE: {}", dataSize);
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
            Long res = this.timeBehaviour(generator, time, iter);
            if(res != null) time = res;

            TreeVertex vert = generator.generate(); // generate random graph
            double result = this.tester.test(vert);

            this.foundRandomExpression(result, vert);
            if(maxResult < result) {
                this.analysedResults.add(new SimpleResultContainer(result, vert));
                logger.info("Expression found {} / {}", this.analysedResults.size(), this.partAmount);
                if(this.analysedResults.size() >= this.partAmount) {
                    for(var foundRes : this.analysedResults) {
                        this.foundResult(
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
        throw new RuntimeException("Unsupported!");
    }

    public abstract void foundResult(double grade, TreeVertex vertex);

    public abstract void foundRandomExpression(double grade, TreeVertex vertex);


    /**
     * Method that be invoked around every 30 seconds in the same thread as search() function
     *
     * @param generator Generator of random graphs,
     *                  required for change probability of type of select AlgebraicVertex in random drawing
     * @param time Old execution time
     * @param iter Number of iteration of search() loop (one loop = generating tree, evaluating, grading)
     * @return General time
     */
    public Long timeBehaviour(ExpressGraphGenerator generator, long time, int iter) {
        double timeChange = System.currentTimeMillis() - time;
        if(timeChange > 30000) { // that's simple controller
            if(generator.getVertexChance() < 36) {
                generator.setVertexChance(generator.getVertexChance() + 1);
            }
            logger.info("Thinking... (Iteration: " + iter + ")");
            return System.currentTimeMillis();
        }
        return null;
    }
}