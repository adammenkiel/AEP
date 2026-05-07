package pl.publicprojects.predictor.model.models;

import lombok.Getter;
import lombok.Setter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;
import pl.publicprojects.predictor.model.AbstractModel;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;

import java.io.IOException;

@Getter
public abstract class StaticPoolESModel implements AbstractModel {

    private final Interpreter interpreter;
    private final ProxyDataLineContainer proxyDataContainer;
    private final ExpressionStandardModel mainModel;
    private final StandardModel helpfulModel;
    private final int amount;
    private final StaticPoolESModel model = this;
    private long startTime = -1;
    private final double gradeResult;
    private int rawDataTableSize = -1;

    @Setter
    private boolean search = true;

    public StaticPoolESModel(Interpreter interpreter, ProxyDataLineContainer proxyDataContainer, int amount, double gradeResult) {
        this.proxyDataContainer = proxyDataContainer;
        this.interpreter = interpreter;
        this.amount = amount;
        this.gradeResult = gradeResult;

        this.helpfulModel = new StandardModel(this.interpreter) {
            @Override
            public void foundResult(byte[] bytes, double grade, TreeVertex vertex) {}

            @Override
            public void foundRandomExpression(byte[] bytes, double grade, TreeVertex vertex) {
                if(grade >= gradeResult) {
                    proxyDataContainer.getExpressionList().add(bytes);

                    int size = proxyDataContainer.getExpressionList().size();
                    System.out.println("Found expression " + size + " / " + amount);
                    System.out.println("$" + (size + rawDataTableSize) + "$ = " +vertex.toString());
                    System.out.println("Grade " + grade + " qualityGrade " + gradeResult);
                    if(size >= amount) {
                        System.out.println("Finished!");
                        this.setSearch(false);
                    }
                }
            }

            @Override
            public void loadData() throws Exception {}
        };

        this.mainModel = new ExpressionStandardModel(interpreter) {
            @Override
            public void foundResult(byte[] bytes, double grade, TreeVertex vertex) {
                model.foundResult(bytes, grade, vertex);
            }

            @Override
            public void foundRandomExpression(byte[] bytes, double grade, TreeVertex vertex) {
                model.foundRandomExpression(bytes, grade, vertex);
            }

            @Override
            public void loadData() throws Exception {}
        };
    }

    @Override
    public void search() throws IOException {
        this.startTime = System.currentTimeMillis();
        this.helpfulModel.search();
        this.debugInterpreter();
        this.mainModel.search();
    }

    private void debugInterpreter() {
        //this.interpreter.get
        System.out.println("DEBUGGING " + this.interpreter.getDataMap().size());
        this.interpreter.getDataMap().forEach((i, data) -> {
            System.out.println("NameId: " + i + " " + data);
        });
    }

    @Override
    public double test(byte[] bytes) throws IOException {
        throw new RuntimeException("Unsupported function!");
    }

    public void addData(StandardDataLineContainer data) {
        this.rawDataTableSize = data.getSize() - 2;
        this.getMainModel().getRawData().add(data);
        this.getHelpfulModel().getRawData().add(data.getRawData());
    }

    public ExpressGraphGenerator getGenerator() {
        return this.mainModel.getGenerator();
    }

    public abstract void foundResult(byte[] bytes, double grade, TreeVertex vertex);

    public abstract void foundRandomExpression(byte[] bytes, double grade, TreeVertex vertex);

}
