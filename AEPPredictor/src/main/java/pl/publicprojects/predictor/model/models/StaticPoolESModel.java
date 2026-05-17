package pl.publicprojects.predictor.model.models;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;
import pl.publicprojects.predictor.model.AbstractModel;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.tester.AbstractTester;

import java.io.IOException;

/**
 * Works similar to PoolESModel, the just one difference is it don't search a threshold
 * just user need to set one in constructor (gradeResult)
 */
@Getter
public abstract class StaticPoolESModel implements AbstractModel {

    private final Interpreter interpreter;
    private final ProxyDataLineContainer proxyDataContainer;
    private final TotalDataContainer totalDataContainer;
    private final ExpressionStandardModel mainModel;
    private final AbstractTester<TreeVertex> mainModelTester;
    private final StandardModel helpfulModel;
    private final AbstractTester<TreeVertex> helpfulModelTester;
    private final int amount;
    private final StaticPoolESModel model = this;
    private long startTime = -1;
    private final double gradeResult;
    private int rawDataTableSize = -1;

    @Getter
    private static final Logger logger = LoggerFactory.getLogger(StandardModel.class);

    @Setter
    private boolean search = true;


    /**
     * @param interpreter For evaluate expressions in helpfulModel(StandardModel) and mainModel(ExpressionStandardModel)
     * @param proxyDataContainer For extending dataset table input
     * @param totalDataContainer For parse results of expressions and create variables when it's necessary
     * @param helpfulModelTester Test way that begin expressions will be graded
     * @param mainModelTester Test way that final expressions will be graded
     * @param amount Amount of begin expressions for find by StandardModel to analyze by ExpressionStandardModel
     * @param gradeResult Threshold for decide if test result is good or not
     */
    public StaticPoolESModel(
            Interpreter interpreter,
            ProxyDataLineContainer proxyDataContainer,
            TotalDataContainer totalDataContainer,
            AbstractTester<TreeVertex> helpfulModelTester,
            AbstractTester<TreeVertex> mainModelTester,
            int amount,
            double gradeResult
    ) {
        this.proxyDataContainer = proxyDataContainer;
        this.totalDataContainer = totalDataContainer;
        this.helpfulModelTester = helpfulModelTester;
        this.mainModelTester = mainModelTester;
        this.interpreter = interpreter;
        this.amount = amount;
        this.gradeResult = gradeResult;

        //For find some good-graded expressions and put them to mainModel as new columns in data set
        this.helpfulModel = new StandardModel(this.interpreter, this.totalDataContainer, this.helpfulModelTester) {

            @Override
            public void foundResult(double grade, TreeVertex vertex) {}

            /**
             * Search expressions satisfies a condition test(expression) > gradeResult and
             * ends searching when amount of expressions exceeds fixed value (this.amount)
             */
            @Override
            public void foundRandomExpression(double grade, TreeVertex vertex) {
                if(grade >= gradeResult) {
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

            @Override
            public void loadData() throws Exception {}
        };
        this.helpfulModelTester.setVariables(this.helpfulModel.getVariables());

        // Main model that will generate good-graded expressions
        // based on helpFul model expressions and put new expressions as new column of dataset
        this.mainModel = new ExpressionStandardModel(interpreter, this.totalDataContainer, this.mainModelTester) {

            /**
             * Invokes StaticPoolEsModel#foundResult
             */
            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                model.foundResult(grade, vertex);
            }

            /**
             * Invokes StaticPoolEsModel#foundRandomExpression
             */
            @Override
            public void foundRandomExpression(double grade, TreeVertex vertex) {
                model.foundRandomExpression(grade, vertex);
            }

            @Override
            public void loadData() throws Exception {}
        };
        this.mainModelTester.setVariables(this.mainModel.getVariables());
    }

    /**
     * Method responsible for searching solutions, first search expressions with grade bigger than this.gradeResult,
     * later creates new expressions using expressions that are in ProxyDataContainer
     * @throws IOException Throws when helpFul or mainModel throws
     */
    @Override
    public void search() throws IOException {
        this.startTime = System.currentTimeMillis();
        this.helpfulModel.search();
        this.mainModel.search();
    }


    @Override
    public double test(byte[] bytes) throws IOException {
        throw new RuntimeException("Unsupported function!");
    }

    /**
     * Function for add data in loadData Method
     * @param data Line of dataset table
     */
    public void addData(StandardDataLineContainer data) {
        this.rawDataTableSize = data.getSize() - 2;
        this.getTotalDataContainer().getRawData().add(data);
        //this.getMainModel().getTotalDataContainer().getRawData().add(data);
        //this.getHelpfulModel().getTotalDataContainer().getRawData().add(data);
    }

    /**
     * @return Returns ExpressionGraphGenerator, class for generate expression trees
     */
    public ExpressGraphGenerator getGenerator() {
        return this.mainModel.getGenerator();
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
