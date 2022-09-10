package com.github.cobrijani.tempharm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Algorithm {

    BRUTE_FORCE(new BruteForceTemporalHarmonizerAlgorithm()),
    OPTIMIZED(new OptimizedTemporalHarmonizerAlgorithm());

    private final TemporalHarmonizerAlgorithm alg;
}
