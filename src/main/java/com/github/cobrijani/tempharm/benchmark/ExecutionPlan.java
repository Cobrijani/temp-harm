package com.github.cobrijani.tempharm.benchmark;

import com.github.cobrijani.tempharm.*;
import org.openjdk.jmh.annotations.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
public class ExecutionPlan {
    @Param({ "100", "1000", "10000", "100000" })
    public int numberOfDateRanges;

    public BruteForceTemporalHarmonizerAlgorithm bruteForce;
    public OptimizedTemporalHarmonizerAlgorithm optimized;

    public OptimizedTemporalHarmonizerAlgorithm lsdOptimized;
    public List<DateRange> dateRanges;

    @Setup(Level.Invocation)
    public void setUp() {
        bruteForce = new BruteForceTemporalHarmonizerAlgorithm();
        optimized = new OptimizedTemporalHarmonizerAlgorithm(new NormalSortingStrategy());
        lsdOptimized = new OptimizedTemporalHarmonizerAlgorithm(new LsdRadixSortingStrategy());
        dateRanges = IntStream.range(0, numberOfDateRanges)
                .mapToObj(x -> createRandomDateRange())
                .collect(Collectors.toList());
    }

    private DateRange createRandomDateRange() {
        var first = randomLocalDate();
        var second = randomLocalDate();

        if (first.isAfter(second)) {
            return new DateRange(second, first);
        } else {
            return new DateRange(first, second);
        }
    }

    private LocalDate randomLocalDate() {
        var d1 = LocalDate.EPOCH;
        var d2 = LocalDate.MAX;

        Date randomDate = new Date(ThreadLocalRandom.current()
                .nextLong(d1.toEpochDay(), d2.toEpochDay()));

        return randomDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
