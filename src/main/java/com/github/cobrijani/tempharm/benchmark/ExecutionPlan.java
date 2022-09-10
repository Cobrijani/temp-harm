package com.github.cobrijani.tempharm.benchmark;

import com.github.cobrijani.tempharm.BruteForceTemporalHarmonizerAlgorithm;
import com.github.cobrijani.tempharm.DateRange;
import com.github.cobrijani.tempharm.OptimizedTemporalHarmonizerAlgorithm;
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

    @Param({ "100", "200", "300", "500", "1000" })
    public int numberOfDateRanges;

    public BruteForceTemporalHarmonizerAlgorithm bruteForce;
    public OptimizedTemporalHarmonizerAlgorithm optimized;
    public List<DateRange> dateRanges;

    @Setup(Level.Invocation)
    public void setUp() {
        bruteForce = new BruteForceTemporalHarmonizerAlgorithm();
        optimized = new OptimizedTemporalHarmonizerAlgorithm();
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
