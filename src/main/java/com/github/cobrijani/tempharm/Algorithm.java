package com.github.cobrijani.tempharm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Algorithm {

    BRUTE_FORCE(new BruteForceTemporalHarmonizerAlgorithm()),
    OPTIMIZED_QUICKSORT(new OptimizedTemporalHarmonizerAlgorithm(new NormalSortingStrategy())),
    OPTIMIZED_LSD_RADIX_SORT(new OptimizedTemporalHarmonizerAlgorithm(new LsdRadixSortingStrategy()));

    private final TemporalHarmonizerAlgorithm alg;
}
