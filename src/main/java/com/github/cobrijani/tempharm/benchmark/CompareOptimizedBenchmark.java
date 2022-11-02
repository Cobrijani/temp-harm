package com.github.cobrijani.tempharm.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

public class CompareOptimizedBenchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void benchOptimized(ExecutionPlan plan, Blackhole blackhole) {
        blackhole.consume(plan.optimized.harmonize(plan.dateRanges));
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void benchOptimizedLsd(ExecutionPlan plan, Blackhole blackhole) {
        blackhole.consume(plan.lsdOptimized.harmonize(plan.dateRanges));
    }

}
