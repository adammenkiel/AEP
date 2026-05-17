package pl.publicprojects.predictor.model.models;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
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
 * Abstract class for define predicting models just generating random expressions and save good-graded ones
 * that type of model don't use another ones for build new.
 */
@Getter
public abstract class StandardModel implements AbstractModel {

    private final Interpreter interpreter;
    private final AbstractTester<TreeVertex> tester;

    private final TotalDataContainer totalDataContainer;
    private final List<DataLineContainer> rawData;
    private final List<VariableData> variables = new ArrayList<>();

    @Getter
    private static final Logger logger = LoggerFactory.getLogger(StandardModel.class);

    @Setter
    private boolean search = true;

    /**
     * @param interpreter Required for eval expressions
     * @param totalDataContainer Container of data to use for predict a result
     * @param tester Implementation of method defines how expressions will be tested
     */
    public StandardModel(Interpreter interpreter, TotalDataContainer totalDataContainer, AbstractTester<TreeVertex> tester) {
        this.totalDataContainer = totalDataContainer;
        this.rawData = this.totalDataContainer.getRawData();
        this.interpreter = interpreter;
        this.tester = tester;
        this.tester.setVariables(variables);
    }

    /**
     * Searches and grades result while this.search = true
     * @throws IOException When expressions are incorrect or model configuration isn't correct
     */
    public void search() throws IOException {
        int dataSize = rawData.getFirst().getRawData().length - 1;
        ExpressGraphGenerator generator = new ExpressGraphGenerator(30, 10, dataSize);
        this.variables.addAll(this.getTotalDataContainer().createVariables(dataSize));

        long time = System.currentTimeMillis();
        double maxResult = 0;

        int iter = 0;
        while(this.search) {
            Long res = this.timeBehaviour(generator, time, iter);
            if(res != null) time = res;

            TreeVertex vert = generator.generate(); // generate random graph
            double result = this.tester.test(vert); // test

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
    public double test(byte[] bytes) throws IOException {
        throw new RuntimeException("Unsupported function!");
    }

    /**
     * Method for define what will be happened if any expression will be found
     * That's function is invoked even if score of considered expression is bad
     *
     * @param grade Score of this expression
     * @param vertex Expression in graph form
     */
    public abstract void foundResult(double grade, TreeVertex vertex);

    /**
     * Method for define what will be happened if good-graded expression will be found
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
