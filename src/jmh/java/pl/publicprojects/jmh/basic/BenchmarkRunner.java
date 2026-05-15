package pl.publicprojects.jmh.basic;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        Logger logger = LoggerFactory.getLogger(BenchmarkRunner.class);
        logger.info("Starting...");
        Options opt = new OptionsBuilder()
                .include("pl.publicprojects.jmh.benchmarks.*")
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}
