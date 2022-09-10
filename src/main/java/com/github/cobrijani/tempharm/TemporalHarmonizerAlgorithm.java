package com.github.cobrijani.tempharm;

public interface TemporalHarmonizerAlgorithm {

    Iterable<DateRange> harmonize(Iterable<DateRange> dateRanges);
}
