import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.DoubleVariable;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class ThirdGraphTest {

    public static Map<Double, Double> map = new HashMap<>();
    public static Interpreter i = new Interpreter();
    public static DoubleVariable var = new DoubleVariable(i, 0);

    public static double standardTest(byte[] bytes) throws IOException {
        AtomicReference<Double> max = new AtomicReference<>((double) 0);
        map.forEach((key, value) -> {
            try {
                var.setValue(new DoubleNumber(key));
                double resultValue = (double) i.getAlgebraicExpressionManager()
                        .getResult(bytes)
                        .getValue();
                max.set(Math.max(max.get(), Math.pow((value - resultValue), 2)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return max.get();
    }

    private static Random rand = new Random();

    private static double putVarAndSolve(byte[] bytes, LanguageNumber<?> number) throws IOException {
        var.setValue(number);
        return (double) i.getAlgebraicExpressionManager()
                .getResult(bytes)
                .getValue();
    }

    public static double algebraicTest(int power, byte[] bytes) throws IOException {
        boolean test = true;
        double res = 10000000;
        double bound = 100;
        for(int l = 0; l < power; l++) {
            double xOne = rand.nextDouble(bound) - bound / 2;
            double xTwo = rand.nextDouble(bound) - bound / 2;
            if(xOne == xTwo) continue;
            var.setValue(new DoubleNumber(xOne));
            double resultOne = putVarAndSolve(bytes, new DoubleNumber(xOne));
            double resultTwo = putVarAndSolve(bytes, new DoubleNumber(xTwo));

            double resultSum = putVarAndSolve(bytes, new DoubleNumber(Math.sqrt(xOne * xTwo)));
            double resultSumTwo = putVarAndSolve(bytes, new DoubleNumber((xOne + xTwo) / 2));
            //Math.sqrt(resultOne + resultTwo);

            double localMis = Math.pow(resultSumTwo - resultSum, 2);
            res = Math.min(localMis, res);
            //test = test && localMis < 0.1;
            test = test && !((Math.pow(resultOne - resultTwo, 2) < 0.0001 && Math.pow(resultSum - resultTwo, 2) < 0.0001));
        }

        if(test) return res;
        return -1;
    }

    public static double sumIntegerTest(byte[] bytes) throws IOException {
        AtomicReference<Double> sum = new AtomicReference<>((double) 0);
        map.forEach((key, value) -> {
            try {
                var.setValue(new DoubleNumber(key));
                double resultValue = (double) i.getAlgebraicExpressionManager()
                        .getResult(bytes)
                        .getValue();
                sum.set(sum.get() + Math.abs(value - Math.floor(resultValue/Math.log(resultValue))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return sum.get();
    }

    private static int func(int a) {
        int d = 0;
        for(int i = 1; i < a; i++) {
            if(a % i == 0) {
                d+=1;
            }
        }
        return d;
    }

    public static double riemann(double x) {
        double sum = 0;
        for(int i = 1; i < 10000; i++) {
            sum += 1./Math.pow(i, x);
        }
        return sum;
    }

    public static void standardExperiment() throws IOException {
        Random r = new Random();
        /*
        double l = 0;
        for(int i = 1; i <= 10000; i+=1) {
            if(func(i) == 1) {
                l++;
            }
            if( i <= 100 || i >=9900)
                map.put((double)i, l); //1. / Math.sqrt(2 * Math.PI) * Math.pow(Math.E, -1./2. * Math.pow(i, 2)));
        }
        */
        for(double i = -1; i<1; i+= 0.1) {
            map.put(i, Math.floor(i));
        }
        ExpressGraphGenerator generator = new ExpressGraphGenerator(30, 1, 1);

        var.execute();
        //var.setValue(new DoubleNumber(2));
        String result = "";
        int resultLen = 100000000;
        double min = 10000;
        double time = System.currentTimeMillis();
        int iter = 0;
        while(true) {
            double timeChange = System.currentTimeMillis() - time;
            if(timeChange > 30000) {
                if(generator.getVertexChance() < 36) {
                    generator.setVertexChance(generator.getVertexChance() + 1);
                }
                System.out.println("Thinking... Iteration: " + iter + " vertexChance " + generator.getVertexChance());
                time = System.currentTimeMillis();
            }

            TreeVertex vert = generator.generate();
            byte[] bytes = vert.visit();
            double max = standardTest(bytes);//sumIntegerTest(bytes);
            if(min > max) {
                result = vert.toString();
                System.out.println("Result solution: " + result.replaceAll("\\$0\\$", "x"));
                System.out.println("Test solution " + max);
                min = max;
                resultLen = result.length();
            }
            if(min == max) {
                String res = vert.toString();
                if(resultLen > res.length()) {
                    result = vert.toString();
                    System.out.println("Better solution: " + result.replaceAll("\\$0\\$", "x"));
                    resultLen = res.length();
                }
            }
            //if(max < 0.000001) {
            //    return;
            //}
            iter++;
        }

    }

    public static void propertyExperiment() throws IOException {
        ExpressGraphGenerator generator = new ExpressGraphGenerator(30, 1);

        var.execute();
        //var.setValue(new DoubleNumber(2));
        String result;
        double time = System.currentTimeMillis();
        int iter = 0;
        int resultLen = 100000000;
        double min = 10000;

        while(true) {
            double timeChange = System.currentTimeMillis() - time;
            if(timeChange > 30000) {
                if(generator.getVertexChance() < 36) {
                    generator.setVertexChance(generator.getVertexChance() + 1);
                }
                System.out.println("Thinking... Iteration: " + iter + " vertexChance " + generator.getVertexChance());
                time = System.currentTimeMillis();
            }

            TreeVertex vert = generator.generate();
            byte[] bytes = vert.visit();
            double max = algebraicTest(100,bytes);//sumIntegerTest(bytes);
            if(max != -1) {
                if(min > max) {
                    result = vert.toString();
                    System.out.println("Result solution: " + result.replaceAll("\\$0\\$", "x"));
                    System.out.println("Test solution " + max);
                    min = max;
                    resultLen = result.length();
                }
            }
            iter++;
        }
    }

    public static void main(String[] args) throws IOException {
        propertyExperiment();
    }
}
