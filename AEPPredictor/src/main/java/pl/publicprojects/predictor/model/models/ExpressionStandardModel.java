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
import pl.publicprojects.predictor.model.data.DataLineContainer;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.tester.AbstractTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for defining predictive models generating new expressions
 * and optionally use added expressions as inputs.
 * After adding new element to proxyDataContainer.getExpressionList()
 * and updating variable amount in ExpressGraphGenerator#setVariablesAmount
 * the model treats that expression values as new column of dataset table
 * and use it to generate new results
 */
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

    @Getter
    private static final Logger logger = LoggerFactory.getLogger(ExpressionStandardModel.class);

    @Setter
    private boolean search = true;

    /**
     * @param interpreter Required for eval expressions
     * @param totalDataContainer Dataset table with necessary operations
     * @param tester Implementation of method defines how expressions will be tested
     */
    public ExpressionStandardModel(Interpreter interpreter, TotalDataContainer totalDataContainer, AbstractTester<TreeVertex> tester) {
        this.totalDataContainer = totalDataContainer;
        this.rawData = this.totalDataContainer.getRawData();
        this.interpreter = interpreter;
        this.tester = tester;
        this.tester.setVariables(this.variables);
    }

    /**
     * Method sets max amount Algebraic vertices in ExpressGraphGenerator
     * @param pointLimit Max amount of Algebraic vertices
     */
    public void setTreeLimit(int pointLimit) {
        this.pointLimit = pointLimit;
        this.haveTreeLimits = true;
    }

    /**
     * Searches and grades result while this.search = true
     * @throws IOException When expressions are incorrect or model configuration isn't correct
     */
    public void search() throws IOException {
        int dataSize = totalDataContainer.getRawData().getFirst().getSize() - 1;
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
            final Long res = this.timeBehaviour(generator, time, iter);
            if(res != null) time = res;

            final TreeVertex vert = generator.generate(); // generate random graph
            final double result = this.tester.test(vert); // test

            this.foundRandomExpression(result, vert);
            if(maxResult < result) {
                this.foundResult(result, vert);
                maxResult = result;
            }
            iter++;

        }
    }

    /**
     * Method for inlining from tester in the future.
     */
    @Deprecated
    public double test(byte[] bytes) throws IOException {
        throw new RuntimeException("Unsupported function!");
    }

    /**
     * Method for define what will be happened if any expression will be found
     * That's function is invoked even if score of considered expression is bad
     * This method may be used for adding new expressions that may be used for
     * generating new expressions
     *
     * @param grade Score of this expression
     * @param vertex Expression in graph form
     */
    public abstract void foundResult(double grade, TreeVertex vertex);

    /**
     * Method for define what will be happened if good-graded expression will be found
     * This method may be used for adding new expressions that may be used for
     * generating new expressions
     *
     * @param grade Score of this expression
     * @param vertex Expression in graph form
     */
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
            logger.info("Thinking... (Iteration: {})", iter);
            return System.currentTimeMillis();
        }
        return null;
    }
}