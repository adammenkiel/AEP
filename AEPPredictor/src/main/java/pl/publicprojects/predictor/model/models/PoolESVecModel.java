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

@Deprecated
@Getter
public abstract class PoolESVecModel implements AbstractModel {

    private final Interpreter interpreter;
    private final TotalDataContainer totalDataContainer;
    private final ProxyDataLineContainer proxyDataContainer;
    private final ExpressionStandardModel mainModel;
    private final AbstractTester<TreeVertex> mainModelTester;
    private final ExpressionStandardModel helpfulModel;
    private final AbstractTester<TreeVertex> helpfulModelTester;
    private final long qualityTime;
    private final int amount;
    private final PoolESVecModel model = this;
    private long startTime = -1;
    private double gradeResult = -1;
    private int rawDataTableSize = -1;
    private boolean searching = true;
    private final boolean minTime;

    @Getter
    private static final Logger logger = LoggerFactory.getLogger(PoolESVecModel.class);

    @Setter
    private boolean search = true;

    public PoolESVecModel(
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


        this.helpfulModel = new ExpressionStandardModel(this.interpreter, this.totalDataContainer, this.helpfulModelTester) {
            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                if(!searching) return;
                gradeResult = grade;
                logger.info("{} > {}", System.currentTimeMillis(), startTime + qualityTime);
                logger.info("res {}", System.currentTimeMillis() - (startTime + qualityTime));
                logger.info("Grade {}", grade);
                searching = System.currentTimeMillis() < (startTime + qualityTime);
            }

            @Override
            public void foundRandomExpression(double grade, TreeVertex vertex) {
                if(minTime && searching) {
                    searching = System.currentTimeMillis() < (startTime + qualityTime);
                }
                if(!searching && grade >= gradeResult) {
                    try {
                        byte[] visitedBytes = vertex.visit();
                        proxyDataContainer.getExpressionList().add(visitedBytes);
                    } catch (Exception e) {
                        throw new RuntimeException("Something went wrong!");
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

        this.mainModel = new ExpressionStandardModel(interpreter, this.totalDataContainer, this.mainModelTester) {
            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                model.foundResult(grade, vertex);
            }

            @Override
            public void foundRandomExpression(double grade, TreeVertex vertex) {
                model.foundRandomExpression(grade, vertex);
            }

            @Override
            public void loadData() throws Exception {}
        };
        this.mainModelTester.setVariables(this.mainModel.getVariables());
    }

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

    public void addData(StandardDataLineContainer data) {
        this.rawDataTableSize = data.getSize() - 2;
        this.getMainModel().getTotalDataContainer().getRawData().add(data);
        this.getHelpfulModel().getTotalDataContainer().getRawData().add(data);
    }

    public ExpressGraphGenerator getGenerator() {
        return this.mainModel.getGenerator();
    }

    public abstract void foundResult(double grade, TreeVertex vertex);

    public abstract void foundRandomExpression(double grade, TreeVertex vertex);

}

