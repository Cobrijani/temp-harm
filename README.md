# Introduction

Algorithms that create harmonized list of date ranges based on list of date ranges that have overlaps.

For example consider the following date ranges that overlap.
```
A --- B 
   C----D
```
The result list of the harmonization looks as follows:

```
[(A, C - 1), (C, B), (B + 1, D)]
```
-1 and +1 represent subtraction or addition of one day.

# Benchmark Results

## Throughput

| Number of Date Ranges | Cnt | Score (BF) | Score (OPT) | Error (BF)  |  Error (OPT)  | Units  |
|----------------------:|----:|-----------:|------------:|:-----------:|:-------------:|--------|
|                   100 |   5 |     ≈ 10⁻⁶ |      ≈ 10⁻⁵ |      -      |       -       | ops/ns |
|                   200 |   5 |     ≈ 10⁻⁶ |      ≈ 10⁻⁵ |      -      |       -       | ops/ns |
|                   300 |   5 |     ≈ 10⁻⁷ |      ≈ 10⁻⁵ |      -      |       -       | ops/ns |
|                   500 |   5 |     ≈ 10⁻⁷ |      ≈ 10⁻⁶ |      -      |       -       | ops/ns |
|                  1000 |   5 |     ≈ 10⁻⁸ |      ≈ 10⁻⁶ |      -      |       -       | ops/ns |

## Average Time

| Number of Date Ranges | Cnt |     Score (BF) |  Score (OPT) |  Error (BF) | Error (OPT) | Units |
|----------------------:|----:|---------------:|-------------:|------------:|------------:|------:|
|                   100 |   5 |   772375.697 ± |  53490.993 ± |    1887.405 |     439.354 | ns/op |
|                   200 |   5 |  3129582.144 ± | 115131.978 ± |    3806.883 |     235.230 | ns/op |
|                   300 |   5 |  6995960.544 ± | 177535.090 ± |   34009.448 |    1462.580 | ns/op |
|                   500 |   5 | 20686795.432 ± | 323399.321 ± |   49483.721 |    1558.603 | ns/op |
|                  1000 |   5 | 84206047.240 ± | 714277.690 ± | 1036959.514 |    3992.972 | ns/op |

## Single shot invocation time

| Number of Date Ranges | Cnt |    Score (BF) |  Score (OPT) | Error (BF)  | Error (OPT) | Units |
|----------------------:|----:|--------------:|-------------:|:-----------:|:-----------:|:-----:|
|                   100 |   5 |  20875261.000 |  9502526.000 |      -      |      -      | ns/op |
|                   200 |   5 |  32037848.000 | 11011959.000 |      -      |      -      | ns/op |
|                   300 |   5 |  46083486.000 | 13122494.000 |      -      |      -      | ns/op |
|                   500 |   5 |  74905325.000 | 16014941.000 |      -      |      -      | ns/op |
|                  1000 |   5 | 241499746.000 | 19305947.000 |      -      |      -      | ns/op |




