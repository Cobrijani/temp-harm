package com.github.cobrijani.tempharm.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark                            (iterations)  Mode  Cnt         Score         Error  Units
 * AlgorithmsBenchmark.benchBruteforce           100  avgt    5    756697.079 ±   32043.387  ns/op
 * AlgorithmsBenchmark.benchBruteforce           200  avgt    5   3112915.829 ±   30682.361  ns/op
 * AlgorithmsBenchmark.benchBruteforce           300  avgt    5   7158376.950 ±   33863.680  ns/op
 * AlgorithmsBenchmark.benchBruteforce           500  avgt    5  20272446.585 ±  192995.708  ns/op
 * AlgorithmsBenchmark.benchBruteforce          1000  avgt    5  82661344.240 ± 2655227.424  ns/op
 * AlgorithmsBenchmark.benchOptimized            100  avgt    5     56873.727 ±    5542.525  ns/op
 * AlgorithmsBenchmark.benchOptimized            200  avgt    5    119769.137 ±    2872.382  ns/op
 * AlgorithmsBenchmark.benchOptimized            300  avgt    5    191330.485 ±    6114.553  ns/op
 * AlgorithmsBenchmark.benchOptimized            500  avgt    5    342649.163 ±    5999.150  ns/op
 * AlgorithmsBenchmark.benchOptimized           1000  avgt    5    765703.850 ±   10767.657  ns/op
 */
public class AlgorithmsBenchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

//    @Fork(value = 1, warmups = 1)
    //    @Benchmark
    //    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    //    @BenchmarkMode(Mode.All)
    //    public void benchBruteforce(ExecutionPlan plan, Blackhole blackhole) {
    //        blackhole.consume(plan.bruteForce.harmonize(plan.dateRanges));
    //    }
    //
    //    @Fork(value = 1, warmups = 1)
    //    @Benchmark
    //    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    //    @BenchmarkMode(Mode.All)
    //    public void benchOptimized(ExecutionPlan plan, Blackhole blackhole) {
    //        blackhole.consume(plan.optimized.harmonize(plan.dateRanges));
    //    }

    //    @Fork(value = 1, warmups = 1)
    //    @Benchmark
    //    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    //    @BenchmarkMode(Mode.All)
    //    public void benchOptimizedLsd(ExecutionPlan plan, Blackhole blackhole) {
    //        blackhole.consume(plan.lsdOptimized.harmonize(plan.dateRanges));
    //    }


}
