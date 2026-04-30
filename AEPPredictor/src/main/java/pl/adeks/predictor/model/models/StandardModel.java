package pl.adeks.predictor.model.models;

import lombok.Getter;
import lombok.Setter;
import pl.adeks.language.interpreter.Interpreter;
import pl.adeks.language.interpreter.data.math.LanguageNumber;
import pl.adeks.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.adeks.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.adeks.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.adeks.predictor.graph.TreeVertex;
import pl.adeks.predictor.graph.generator.ExpressGraphGenerator;
import pl.adeks.predictor.model.AbstractModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class StandardModel extends AbstractModel {

    private final Interpreter interpreter;
    private final List<LanguageNumber<?>[]> rawData = new ArrayList<>();
    private final List<DoubleVariable> variables = new ArrayList<>();

    @Setter
    private boolean search = true;

    public StandardModel(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void search() throws IOException {
        int dataSize = rawData.getFirst().length - 1;
        ExpressGraphGenerator generator = new ExpressGraphGenerator(30, 4, dataSize);

        for(int nameId = 0; nameId < dataSize; nameId++) {
            DoubleVariable variable = new DoubleVariable(nameId);
            variable.execute();
            this.variables.add(variable);
        }

        long time = System.currentTimeMillis();
        double maxResult = 0;

        int iter = 0;
        while(this.search) {
            Long res = this.timeBehaviour(generator, time, iter);
            if(res != null) time = res;

            TreeVertex vert = generator.generate(); // generate random graph
            byte[] bytes = vert.visit(); // change to bytes
            double result = this.test(bytes); // test

            this.foundRandomExpression(bytes, result, vert);
            if(maxResult < result) {
                this.foundResult(bytes, result, vert);
                maxResult = result;
            }
            iter++;
        }
    }

    @Deprecated
    private void printBytes(byte[] bytes) {
        String lab = "";
        for(byte b : bytes) {
            lab += ((int)b) + ", ";
        }
        System.out.println(lab);
    }

    public double test(byte[] bytes) throws IOException {
        int fit = 0;
        int general = 0;

        boolean isCorrect = true;
        for(LanguageNumber<?>[] info : this.rawData) {
            boolean correctResult = ((IntegerNumber)info[0]).getValue() == 1;

            for(int i = 0; i < info.length - 1; i++) {
                this.getVariables().get(i).setValue(info[i + 1]);
            }

            double resultDoubleValue = (double) interpreter.getAlgebraicExpressionManager()
                    .getResult(bytes)
                    .getValue();

            if (Double.isInfinite(resultDoubleValue) || Double.isNaN(resultDoubleValue)) {
                isCorrect = false;
                resultDoubleValue = 1;
            }

            boolean guessResult = resultDoubleValue > 0;

            if(guessResult == correctResult) {
                if(guessResult) {
                    int rewardForOne = 1;
                    fit += rewardForOne;
                    general += rewardForOne;
                } else {
                    int rewardForZero = 1;
                    fit += rewardForZero;
                    general += rewardForZero;//rewardForZero;
                }
            } else {
                general += 10;
            }
        }

        return (double)fit / (double) general;
    }

    public abstract void foundResult(byte[] bytes, double grade, TreeVertex vertex);
    public abstract void foundRandomExpression(byte[] bytes, double grade, TreeVertex vertex);


    public Long timeBehaviour(ExpressGraphGenerator generator, long time, int iter) {
        double timeChange = System.currentTimeMillis() - time;
        if(timeChange > 30000) { // that's simple controller
            if(generator.getVertexChance() < 36) {
                generator.setVertexChance(generator.getVertexChance() + 1);
            }
            System.out.println("Thinking... (Iteration: " + iter + ")");
            return System.currentTimeMillis();
        }
        return null;
    }
}
