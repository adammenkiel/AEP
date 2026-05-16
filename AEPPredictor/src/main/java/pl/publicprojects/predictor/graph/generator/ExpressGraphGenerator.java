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
import java.util.SplittableRandom;
import java.util.random.RandomGenerator;

@Getter
@Setter
public class ExpressGraphGenerator {

    //private Random random = new Random();
    //private SplittableRandom random = new SplittableRandom(); // not thread-safe
    private RandomGenerator random = RandomGenerator.of("Xoroshiro128PlusPlus");

    private List<AlgebraicVertex> vertexList = new ArrayList<>();
    private int vertexChance;
    private int numberValues;
    private int variablesAmount;

    private boolean haveLimit = false;
    private int pointLimit;
    private int vertexEndChance;

    private final int MAX_PERCENT = 100;

    public ExpressGraphGenerator() {
        this.vertexChance = 34;
        this.numberValues = 100;
        this.variablesAmount = 1;
    }

    /**
     * @param vertexChance Chance of being drawn an algebraic vertex
     * @param variablesAmount Number of variables
     */
    public ExpressGraphGenerator(int vertexChance, int variablesAmount) {
        this.vertexChance = vertexChance;
        this.numberValues = 100;
        this.variablesAmount = variablesAmount;
    }

    /**
     * @param vertexChance Chance of being drawn an algebraic vertex
     * @param numberValues Scale [0, n] of integers in this expression
     * @param variablesAmount Number of variables
     */
    public ExpressGraphGenerator(int vertexChance, int numberValues, int variablesAmount) {
        this.vertexChance = vertexChance;
        this.numberValues = numberValues;
        this.variablesAmount = variablesAmount;
    }

    /**
     * @return Returns vertexes for build a graph
     */
    private TreeVertex draw() {
        final int a = random.nextInt(MAX_PERCENT);

        final int chanceOne = vertexChance;
        final int chanceTwo = (MAX_PERCENT - chanceOne) / 2;

        int standardAlgebra = random.nextInt(3);
        if(standardAlgebra == 2) standardAlgebra = 3; // temporary

        if(a < chanceOne) return new AlgebraicVertex(standardAlgebra);
        if(a < chanceOne + chanceTwo) return new NumberVertex(new DoubleNumber((random.nextInt(this.numberValues))));
        return new VariableVertex(random.nextInt(this.variablesAmount));
    }

    /**
     * Returns end vertexes when tree surpass the limit (if we set it)
     * @return TreeVertex that isn't the AlgebraicVertex
     */
    private TreeVertex drawEnd() {
        int a = random.nextInt(MAX_PERCENT);
        int chanceOne = vertexEndChance;

        if(a < chanceOne) return new VariableVertex(random.nextInt(this.variablesAmount));
        return new NumberVertex(new DoubleNumber((random.nextInt(this.numberValues))));
    }

    /**
     * Generates random algebraic expression by BFS algorithm
     * @return Returns algebraic expression in TreeVertex form
     */
    public TreeVertex generate() {

        int standardAlgebra = random.nextInt(3);
        if(standardAlgebra == 2) standardAlgebra = 3;

        AlgebraicVertex algebraicVertex = new AlgebraicVertex(standardAlgebra);
        vertexList.add(algebraicVertex);
        int addedPointsAmount = 0;
        final boolean haveLimitConst = haveLimit;
        while(!vertexList.isEmpty()) {
            AlgebraicVertex random = this.vertexList.get(0); //faster than getFirst
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
