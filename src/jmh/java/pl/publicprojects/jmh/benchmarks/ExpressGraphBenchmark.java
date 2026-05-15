package pl.publicprojects.jmh.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import pl.publicprojects.predictor.graph.generator.ExpressGraphGenerator;

import java.util.concurrent.TimeUnit;


/**
 * How much time we need to check one tree
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class ExpressGraphBenchmark {

    private ExpressGraphGenerator expressGraphGenerator;

    @Setup
    public void setup() {
        this.expressGraphGenerator = new ExpressGraphGenerator();
    }

    @Benchmark
    public void expressGenerate(Blackhole blackhole) {
        blackhole.consume(expressGraphGenerator.generate());
    }
}
