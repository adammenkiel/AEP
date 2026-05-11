package pl.publicprojects.predictor.graph.generator;

import lombok.Getter;
import lombok.Setter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.expression.algebra.AlgebraicVertex;
import pl.publicprojects.predictor.graph.expression.algebra.NumberVertex;
import pl.publicprojects.predictor.graph.expression.algebra.VariableVertex;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class ExpressGraphGenerator {

    private Random random = new Random();
    private List<AlgebraicVertex> vertexList = new ArrayList<>();
    private int vertexChance;
    private int numberValues;
    private int variablesAmount;

    private boolean haveLimit = false;
    private int pointLimit;
    private int vertexEndChance;

    public ExpressGraphGenerator() {
        this.vertexChance = 34;
        this.numberValues = 100;
        this.variablesAmount = 1;
    }
    public ExpressGraphGenerator(int vertexChance, int variablesAmount) {
        this.vertexChance = vertexChance;
        this.numberValues = 100;
        this.variablesAmount = variablesAmount;
    }
    public ExpressGraphGenerator(int vertexChance, int numberValues, int variablesAmount) {
        this.vertexChance = vertexChance;
        this.numberValues = numberValues;
        this.variablesAmount = variablesAmount;
    }

    private AlgebraicVertex getRandom() {
        return vertexList.get(random.nextInt(vertexList.size()));
    }

    private TreeVertex draw() {
        int a = random.nextInt(100);

        int chanceOne = vertexChance;
        int chanceTwo = (100 - chanceOne) / 2;

        int standardAlgebra = random.nextInt(3);
        if(standardAlgebra == 2) standardAlgebra = 3;

        return switch(a) {
            default -> {
                if(a < chanceOne) yield new AlgebraicVertex(standardAlgebra);
                if(a < chanceOne + chanceTwo) yield new NumberVertex(new DoubleNumber((random.nextInt(this.numberValues))));
                yield new VariableVertex(random.nextInt(this.variablesAmount));
            }
        };
    }

    private TreeVertex drawEnd() {
        int a = random.nextInt(100);
        int chanceOne = vertexEndChance;

        return switch(a) {
            default -> {
                if(a < chanceOne) yield new VariableVertex(random.nextInt(this.variablesAmount));
                yield new NumberVertex(new DoubleNumber((random.nextInt(this.numberValues))));
            }
        };
    }

    public TreeVertex generate() {

        int standardAlgebra = random.nextInt(3);
        if(standardAlgebra == 2) standardAlgebra = 3;

        AlgebraicVertex algebraicVertex = new AlgebraicVertex(standardAlgebra);
        vertexList.add(algebraicVertex);
        int addedPointsAmount = 0;
        final boolean haveLimitConst = haveLimit;
        while(!vertexList.isEmpty()) {
            AlgebraicVertex random = this.getRandom();

            TreeVertex createdChild = haveLimitConst && this.pointLimit < addedPointsAmount ? this.drawEnd() : this.draw();

            random.addChild(createdChild);
            if(createdChild instanceof AlgebraicVertex alg) {
                this.vertexList.add(alg);
            }
            if(!random.hasPlace()) {
                this.vertexList.remove(random);
            }
            if(haveLimitConst) addedPointsAmount += 1;
        }
        return algebraicVertex;
    }
}
