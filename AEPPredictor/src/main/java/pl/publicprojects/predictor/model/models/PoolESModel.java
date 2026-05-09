package pl.publicprojects.predictor.model.models;

import lombok.Getter;
import lombok.Setter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;
import pl.publicprojects.predictor.model.AbstractModel;
import pl.publicprojects.predictor.model.data.DataLineContainer;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;

import java.io.IOException;

@Getter
public abstract class PoolESModel implements AbstractModel {

    private final Interpreter interpreter;
    private final ProxyDataLineContainer proxyDataContainer;
    private final ExpressionStandardModel mainModel;
    private final TotalDataContainer totalDataContainer;
    private final StandardModel helpfulModel;
    private final long qualityTime;
    private final int amount;
    private final PoolESModel model = this;
    private long startTime = -1;
    private double gradeResult = -1;
    private int rawDataTableSize = -1;
    private boolean searching = true;
    private final boolean minTime;

    @Setter
    private boolean search = true;

    public PoolESModel(Interpreter interpreter, ProxyDataLineContainer proxyDataContainer, TotalDataContainer totalDataContainer, long qualityTime, int amount, boolean minTime) {
        this.proxyDataContainer = proxyDataContainer;
        this.totalDataContainer = totalDataContainer;
        this.interpreter = interpreter;
        this.qualityTime = qualityTime;
        this.amount = amount;
        this.minTime = minTime;

        this.helpfulModel = new StandardModel(this.interpreter) {
            @Override
            public void foundResult(byte[] bytes, double grade, TreeVertex vertex) {
                if(!searching) return;
                gradeResult = grade;
                System.out.println(System.currentTimeMillis() + " > " + (startTime + qualityTime));
                System.out.println("res " + (System.currentTimeMillis() - (startTime + qualityTime)));
                System.out.println("Grade " + grade);
                searching = System.currentTimeMillis() < (startTime + qualityTime);
            }

            @Override
            public void foundRandomExpression(byte[] bytes, double grade, TreeVertex vertex) {
                if(minTime && searching) {
                    searching = System.currentTimeMillis() < (startTime + qualityTime);
                }
                if(!searching && grade >= gradeResult) {
                    proxyDataContainer.getExpressionList().add(bytes);
                    //mainModel.getGenerator().setVariablesAmount(mainModel.getGenerator().getVariablesAmount() + 1);
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

        this.mainModel = new ExpressionStandardModel(interpreter, this.totalDataContainer) {
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

    public void setMainModelTreeLimit(int pointLimit) {
        this.mainModel.setTreeLimit(pointLimit);
    }

    @Override
    public double test(byte[] bytes) throws IOException {
        throw new RuntimeException("Unsupported function!");
    }

    public void addData(DataLineContainer data) {
        this.rawDataTableSize = data.getSize() - 2;
        this.getMainModel().getTotalDataContainer().getRawData().add(data);
        this.getHelpfulModel().getRawData().add(data.getRawData());
    }

    public ExpressGraphGenerator getGenerator() {
        return this.mainModel.getGenerator();
    }

    public abstract void foundResult(byte[] bytes, double grade, TreeVertex vertex);

    public abstract void foundRandomExpression(byte[] bytes, double grade, TreeVertex vertex);

}
