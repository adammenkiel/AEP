package pl.publicprojects.predictor.model.models;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;
import pl.publicprojects.predictor.model.AbstractModel;
import pl.publicprojects.predictor.model.data.DataLineContainer;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.tester.AbstractTester;

import java.io.IOException;


/**
 * Abstract class of model that have two stages:
 *      - stage 1: StandardModel at first fix a threshold for decide which expression will be accepted or not
 *      threshold is a maximum of test scores of expressions found in allocated time (that's risky when
 *      that threshold will be big), later accept every expression with score bigger than that score and adds it
 *      into ProxyDataContainer
 *      - stage 2: ExpressionStandardModel use founded expressions to build new expressions, that new expressions
 *      also will be put into ProxyDataContainer so will be used for build further expressions too
 */
@Getter
public abstract class PoolESModel implements AbstractModel {

    private final Interpreter interpreter;
    private final ProxyDataLineContainer proxyDataContainer;
    private final ExpressionStandardModel mainModel;
    private final AbstractTester<TreeVertex> mainModelTester;
    private final TotalDataContainer totalDataContainer;
    private final StandardModel helpfulModel;
    private final AbstractTester<TreeVertex> helpfulModelTester;
    private final long qualityTime;
    private final int amount;
    private final PoolESModel model = this;
    private long startTime = -1;
    private double gradeResult = -1;
    private int rawDataTableSize = -1;
    private boolean searching = true;
    private final boolean minTime;

    @Getter
    private static final Logger logger = LoggerFactory.getLogger(PoolESModel.class);

    /**
     * @param interpreter For evaluate expressions in helpfulModel(StandardModel) and mainModel(ExpressionStandardModel)
     * @param proxyDataContainer For extending dataset table input
     * @param totalDataContainer For parse results of expressions and create variables when it's necessary
     * @param helpfulModelTester Test way that begin expressions will be graded
     * @param mainModelTester Test way that final expressions will be graded
     * @param qualityTime Time required to wait for determine quality threshold that
     *                    new expressions will be accepted and saved into proxy-container or not,
     *                    that solution is temporary as there is a chance to get very rare result while
     *                    determining it, so that would be unusual to found expressions satisfying condition
     *                    test(expression) > grade
     * @param amount Amount of begin expressions for find by StandardModel to analyze by ExpressionStandardModel
     * @param minTime Determines if continue to search better threshold grade after time elapses
     */
    public PoolESModel(
            Interpreter interpreter,
            ProxyDataLineContainer proxyDataContainer,
            TotalDataContainer totalDataContainer,
            AbstractTester<TreeVertex> helpfulModelTester,
            AbstractTester<TreeVertex> mainModelTester,
            long qualityTime,
            int amount,
            boolean minTime
    ) {
        this.proxyDataContainer = proxyDataContainer;
        this.totalDataContainer = totalDataContainer;
        this.helpfulModelTester = helpfulModelTester;
        this.mainModelTester = mainModelTester;
        this.interpreter = interpreter;
        this.qualityTime = qualityTime;
        this.amount = amount;
        this.minTime = minTime;

        //For find some good-graded expressions and put them to mainModel as new columns in data set
        this.helpfulModel = new StandardModel(this.interpreter, totalDataContainer, this.helpfulModelTester) {

            /**
             * Sets threshold for minimal good grade that standard model will accept an expression
             */
            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                if(!searching) return;
                gradeResult = grade;
                logger.info("Change minimum score to: {}", grade);
                logger.info("Continue searching: {}", !(System.currentTimeMillis() - (startTime + qualityTime) > 0));
                searching = System.currentTimeMillis() < (startTime + qualityTime);
            }

            /**
             * Search expressions satisfies a condition test(expression) > threshold score and
             * ends searching when amount of expressions exceeds fixed value (this.amount)
             */
            @Override
            public void foundRandomExpression(double grade, TreeVertex vertex) {
                if(minTime && searching) {
                    searching = System.currentTimeMillis() < (startTime + qualityTime);
                }
                if(!searching && grade >= gradeResult) {
                    try {
                        proxyDataContainer.getExpressionList().add(vertex.visit());
                    } catch (Exception e) {
                        throw new RuntimeException("Something went wrong!", e);
                    }

                    int size = proxyDataContainer.getExpressionList().size();

                    logger.info("Found expression {} / {}", size, amount);
                    logger.info("${}$ = {}", size + rawDataTableSize, vertex.toString());
                    logger.info("Grade {} qualityGrade {}", grade, gradeResult);
                    if(size >= amount) {
                        logger.info("Finished!");
                        this.setSearch(false);
                    }
                }
            }

            /**
             * We don't load data here because PoolESModel class is responsible for it
             */
            @Override
            public void loadData() throws Exception {}
        };
        this.helpfulModelTester.setVariables(this.helpfulModel.getVariables());

        // Main model that will generate good-graded expressions
        // based on helpFul model expressions and put new expressions as new column of dataset
        this.mainModel = new ExpressionStandardModel(interpreter, this.totalDataContainer, this.mainModelTester) {
            /**
             * Invokes PoolESModel#foundResult
             */
            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                model.foundResult(grade, vertex);
            }

            /**
             * Invokes PoolESModel#foundRandomExpression
             */
            @Override
            public void foundRandomExpression(double grade, TreeVertex vertex) {
                model.foundRandomExpression(grade, vertex);
            }

            /**
             * We don't load data here because PoolESModel class is responsible for it
             */
            @Override
            public void loadData() throws Exception {}

        };
        this.mainModelTester.setVariables(this.mainModel.getVariables());
    }

    /**
     * Method responsible for searching solutions, first search expressions with grade bigger than threshold value,
     * later creates new expressions using expressions that are in ProxyDataContainer
     * @throws IOException Throws when helpFul or mainModel throws
     */
    @Override
    public void search() throws IOException {
        this.startTime = System.currentTimeMillis();
        this.helpfulModel.search();
        this.mainModel.search();
    }

    /**
     * Sets Limit of algebraic vertices in one expression
     * @param pointLimit Limit of algebraic vertices
     */
    public void setMainModelTreeLimit(int pointLimit) {
        this.mainModel.setTreeLimit(pointLimit);
    }

    @Override
    public double test(byte[] bytes) throws IOException {
        throw new RuntimeException("Unsupported function!");
    }

    /**
     * Function for add data in loadData Method
     * @param data Line of dataset table
     */
    public void addData(DataLineContainer data) {
        this.rawDataTableSize = data.getSize() - 2;
        this.getTotalDataContainer().getRawData().add(data);
        //this.getHelpfulModel().getTotalDataContainer().getRawData().add(data);
    }

    /**
     * @return Returns ExpressionGraphGenerator, class for generate expression trees
     */
    public ExpressGraphGenerator getGenerator() {
        return this.mainModel.getGenerator();
    }

    /**
     * Sets if continue search or not
     * @param searching Condition if continue searching or not
     */
    public void setSearch(boolean searching) {
        this.helpfulModel.setSearch(searching);
        this.mainModel.setSearch(searching);
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

}
