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

        this.helpfulModel = new StandardModel(this.interpreter, this.totalDataContainer, this.helpfulModelTester) {
            @Override
            public void foundResult(double grade, TreeVertex vertex) {}

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
