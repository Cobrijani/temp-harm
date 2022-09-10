package com.github.cobrijani.tempharm;

import com.github.cobrijani.tempharm.usecases.UseCase;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class AlgorithmCorrectnessTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("allCases")
    void optimized(String name, List<DateRange> inputs, List<Tuple> output) {
         runTests(inputs, output, new OptimizedTemporalHarmonizerAlgorithm());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allCases")
    void bruteForce(String name, List<DateRange> inputs, List<Tuple> output) {
        runTests(inputs, output, new BruteForceTemporalHarmonizerAlgorithm());
    }

    private static Stream<Arguments> allCases() {
        return Stream.of(
                bigIntervalOverTwoSmall(),
                bigIntervalOverTwoSmallInf(),
                noOverlap(),
                noOverlapInf(),
                simpleOverlap(),
                simpleOverlapInf(),
                twoIntervalsSameFrom(),
                twoIntervalsSameFromInf(),
                twoIntervalsSameTo(),
                twoIntervalsSameToInf(),
                dotSampleCase(),
                sameDateSample());
    }

    private void runTests(List<DateRange> inputs, List<Tuple> output, TemporalHarmonizerAlgorithm algorithm) {
        var result = algorithm.harmonize(inputs);
        printResult(result);
        assertSameIntervals(inputs, result);
        assertThat(result)
                .extracting(DateRange::getFrom, DateRange::getTo)
                .containsExactly(output.toArray(Tuple[]::new));
    }

    private static UseCase twoIntervalsSameFrom() {
        /*
         *  A--B
         *  C----D
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = LocalDate.of(2021, 1, 3);
        LocalDate c = a;
        LocalDate d = LocalDate.of(2021, 2, 3);
        return UseCase.builder()
                .name("Use case two intervals same from")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d)))
                .output(List.of(
                        tuple(a, b),
                        tuple(b.plusDays(1), d)
                ))
                .build();
    }

    private static UseCase twoIntervalsSameFromInf() {
        /*
         *  A--B
         *  C----D
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = LocalDate.of(2021, 1, 3);
        LocalDate c = a;
        LocalDate d = null;
        return UseCase.builder()
                .name("Use case two intervals same from (inf)")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d)))
                .output(List.of(
                        tuple(a, b),
                        tuple(b.plusDays(1), d)
                ))
                .build();
    }

    private static UseCase twoIntervalsSameTo() {
        /*
         *    A--B
         *  C----D
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = LocalDate.of(2021, 1, 3);
        LocalDate c = LocalDate.of(2020, 2, 1);
        LocalDate d = b;
        return UseCase.builder()
                .name("Use case two intervals same to")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d)))
                .output(List.of(
                        tuple(c, a.minusDays(1)),
                        tuple(a, b)
                ))
                .build();
    }

    private static UseCase twoIntervalsSameToInf() {
        /*
         *    A--B (inf)
         *  C----D (inf)
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = null;
        LocalDate c = LocalDate.of(2020, 2, 1);
        LocalDate d = b;
        return UseCase.builder()
                .name("Use case two intervals same to (inf)")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d)))
                .output(List.of(
                        tuple(c, a.minusDays(1)),
                        tuple(a, b)
                ))
                .build();
    }

    private static UseCase bigIntervalOverTwoSmall() {
        /*
         *  A--B
         *     C--D
         * E---------F
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = LocalDate.of(2021, 1, 3);
        LocalDate c = LocalDate.of(2021, 2, 1);
        LocalDate d = LocalDate.of(2021, 2, 3);
        LocalDate e = LocalDate.of(2020, 2, 1);
        LocalDate f = LocalDate.of(2021, 2, 5);
        return UseCase.builder()
                .name("Use case with three intervals where third interval overlaps two other")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d),
                        createDateRange(e, f)))
                .output(List.of(tuple(e, a.minusDays(1)),
                        tuple(a, b),
                        tuple(b.plusDays(1), c.minusDays(1)),
                        tuple(c, d),
                        tuple(d.plusDays(1), f)))
                .build();
    }

    private static UseCase bigIntervalOverTwoSmallInf() {
        /*
         *  A--B
         *     C--D
         * E----------------F (Inf)
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = LocalDate.of(2021, 1, 3);
        LocalDate c = LocalDate.of(2021, 2, 1);
        LocalDate d = LocalDate.of(2021, 2, 3);
        LocalDate e = LocalDate.of(2020, 2, 1);
        LocalDate f = null;
        return UseCase.builder()
                .name("Use case with three intervals where third inf interval overlaps two other")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d),
                        createDateRange(e, f)))
                .output(List.of(tuple(e, a.minusDays(1)),
                        tuple(a, b),
                        tuple(b.plusDays(1), c.minusDays(1)),
                        tuple(c, d),
                        tuple(d.plusDays(1), f)))
                .build();
    }

    private static UseCase noOverlap() {
        /*
         *  A--B
         *      C--D
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = LocalDate.of(2021, 1, 3);
        LocalDate c = LocalDate.of(2021, 2, 1);
        LocalDate d = LocalDate.of(2021, 2, 3);
        return UseCase.builder()
                .name("Use case where there are not overlaps")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d)))
                .output(List.of(
                        tuple(a, b),
                        tuple(c, d)))
                .build();
    }

    private static UseCase noOverlapInf() {
        /*
         *  A--B
         *      C--D
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = LocalDate.of(2021, 1, 3);
        LocalDate c = LocalDate.of(2021, 2, 1);
        LocalDate d = null;
        return UseCase.builder()
                .name("Use case where there are not overlaps (inf)")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d)))
                .output(List.of(
                        tuple(a, b),
                        tuple(c, d)))
                .build();
    }

    private static UseCase simpleOverlap() {
        /*
         *  A--B
         *   C--D
         */
        var a = LocalDate.of(2021, 1, 1);
        var b = LocalDate.of(2021, 1, 3);
        var c = LocalDate.of(2021, 1, 2);
        var d = LocalDate.of(2021, 2, 3);

        return UseCase.builder()
                .name("Simple overlap")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d)))
                .output(List.of(
                        tuple(a, c.minusDays(1)),
                        tuple(c, b),
                        tuple(b.plusDays(1), d)))
                .build();
    }

    private static UseCase simpleOverlapInf() {
        /*
         *  A--B
         *   C--D (inf)
         */
        var a = LocalDate.of(2021, 1, 1);
        var b = LocalDate.of(2021, 1, 3);
        var c = LocalDate.of(2021, 1, 2);
        LocalDate d = null;

        return UseCase.builder()
                .name("Simple overlap (inf)")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d)))
                .output(List.of(
                        tuple(a, c.minusDays(1)),
                        tuple(c, b),
                        tuple(b.plusDays(1), d)))
                .build();
    }

    private static UseCase dotSampleCase() {
        /*
         *  A--B
         *           C--D
         *       E-F
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = a.plusDays(3);
        LocalDate c = b.plusMonths(1);
        LocalDate d = b.plusMonths(1).plusDays(10);
        LocalDate e = b.plusDays(10);
        LocalDate f = e;

        return UseCase.builder()
                .name("Dot simple case")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d),
                        createDateRange(e, f)))
                .output(List.of(
                        tuple(a, b),
                        tuple(e, f),
                        tuple(c, d)))
                .build();
    }

    private static UseCase sameDateSample() {
        /*
         *  A--B
         *     C--D
         */
        LocalDate a = LocalDate.of(2021, 1, 1);
        LocalDate b = a.plusDays(3);
        LocalDate c = b;
        LocalDate d = b.plusMonths(1);

        return UseCase.builder()
                .name("Same date sample")
                .inputs(List.of(createDateRange(a, b),
                        createDateRange(c, d)))
                .output(List.of(
                        tuple(a, b),
                        tuple(c.plusDays(1), d)))
                .build();
    }

    @ParameterizedTest
    @EnumSource(Algorithm.class)
    void harmonizeTemporalIntersections_1(Algorithm algorithm) {

        var given = List.of(createDateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 31)),
                createDateRange(LocalDate.of(2023, 1, 1), null),
                createDateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 3, 31)),
                createDateRange(LocalDate.of(2021, 4, 1), LocalDate.of(2021, 5, 31)),
                createDateRange(LocalDate.of(2021, 6, 1), LocalDate.of(2021, 6, 16)),
                createDateRange(LocalDate.of(2021, 6, 17), LocalDate.of(2021, 6, 17)),
                createDateRange(LocalDate.of(2021, 6, 18), LocalDate.of(2021, 6, 30)),
                createDateRange(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 9, 30)),
                createDateRange(LocalDate.of(2021, 10, 1), LocalDate.of(2022, 3, 31)),
                createDateRange(LocalDate.of(2022, 4, 1), LocalDate.of(2022, 6, 30)),
                createDateRange(LocalDate.of(2022, 10, 1), null));

        // WHEN
        final var result = algorithm.getAlg().harmonize(given);

        // THEN
        printResult(result);
        assertSameIntervals(given, result);
        assertThat(result)
                .extracting(DateRange::getFrom, DateRange::getTo)
                .containsExactly(
                        tuple(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 3, 31)),
                        tuple(LocalDate.of(2021, 4, 1), LocalDate.of(2021, 5, 31)),
                        tuple(LocalDate.of(2021, 6, 1), LocalDate.of(2021, 6, 16)),
                        tuple(LocalDate.of(2021, 6, 17), LocalDate.of(2021, 6, 17)),
                        tuple(LocalDate.of(2021, 6, 18), LocalDate.of(2021, 6, 30)),
                        tuple(LocalDate.of(2021, 7, 1), LocalDate.of(2021, 7, 31)),
                        tuple(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 9, 30)),
                        tuple(LocalDate.of(2021, 10, 1), LocalDate.of(2021, 12, 31)),
                        tuple(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 3, 31)),
                        tuple(LocalDate.of(2022, 4, 1), LocalDate.of(2022, 6, 30)),
                        tuple(LocalDate.of(2022, 10, 1), LocalDate.of(2022, 12, 31)),
                        tuple(LocalDate.of(2023, 1, 1), null)
                );
    }

    @ParameterizedTest
    @EnumSource(Algorithm.class)
    void harmonizeTemporalIntersections_2(Algorithm algorithm) {
        var given = List.of(
                createDateRange(LocalDate.of(1911, 1, 1), LocalDate.of(2021, 12, 11)),
                createDateRange(LocalDate.of(2021, 12, 12), LocalDate.of(2022, 3, 6)),
                createDateRange(LocalDate.of(2022, 3, 7), null),
                createDateRange(LocalDate.of(1911, 1, 1), LocalDate.of(2022, 3, 11)),
                createDateRange(LocalDate.of(2022, 3, 12), LocalDate.of(2022, 3, 16)),
                createDateRange(LocalDate.of(2022, 3, 17), LocalDate.of(2022, 3, 17)),
                createDateRange(LocalDate.of(2022, 3, 18), LocalDate.of(2022, 4, 3)),
                createDateRange(LocalDate.of(2022, 4, 4), LocalDate.of(2023, 12, 10))
        );
        // WHEN
        final var result = algorithm.getAlg().harmonize(
                given);

        // THEN
        printResult(result);
        assertSameIntervals(given, result);
        assertThat(result)
                .extracting(DateRange::getFrom, DateRange::getTo)
                .containsExactly(
                        tuple(LocalDate.of(1911, 1, 1), LocalDate.of(2021, 12, 11)),
                        tuple(LocalDate.of(2021, 12, 12), LocalDate.of(2022, 3, 6)),
                        tuple(LocalDate.of(2022, 3, 7), LocalDate.of(2022, 3, 11)),
                        tuple(LocalDate.of(2022, 3, 12), LocalDate.of(2022, 3, 16)),
                        tuple(LocalDate.of(2022, 3, 17), LocalDate.of(2022, 3, 17)),
                        tuple(LocalDate.of(2022, 3, 18), LocalDate.of(2022, 4, 3)),
                        tuple(LocalDate.of(2022, 4, 4), LocalDate.of(2023, 12, 10)),
                        tuple(LocalDate.of(2023, 12, 11), null)
                );
    }

    @ParameterizedTest
    @EnumSource(Algorithm.class)
    void harmonizeTemporalIntersections_3(Algorithm algorithm) {
        var given = List.of(
                createDateRange(LocalDate.of(2019, 12, 15), LocalDate.of(2022, 2, 19)),
                createDateRange(LocalDate.of(2022, 4, 11), LocalDate.of(2022, 9, 4)),
                createDateRange(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 12, 10)),
                createDateRange(LocalDate.of(2022, 12, 11), null),
                createDateRange(LocalDate.of(2019, 12, 15), LocalDate.of(2022, 2, 19))
        );
        // WHEN
        final var result = algorithm.getAlg().harmonize(
                given);

        // THEN
        printResult(result);
        assertSameIntervals(given, result);
        assertThat(result)
                .extracting(DateRange::getFrom, DateRange::getTo)
                .containsExactly(
                        tuple(LocalDate.of(2019, 12, 15), LocalDate.of(2022, 2, 19)),
                        tuple(LocalDate.of(2022, 4, 11), LocalDate.of(2022, 9, 4)),
                        tuple(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 12, 10)),
                        tuple(LocalDate.of(2022, 12, 11), null)
                );
    }

    @ParameterizedTest
    @EnumSource(Algorithm.class)
    void harmonizeTemporalIntersections_4(Algorithm algorithm) {
        var given = List.of(
                createDateRange(LocalDate.of(2021, 10, 10), LocalDate.of(2022, 12, 10)),
                createDateRange(LocalDate.of(2021, 10, 10), LocalDate.of(2022, 12, 10)));
        // WHEN
        final var result = algorithm.getAlg().harmonize(
                given
        );

        // THEN
        printResult(result);
        assertSameIntervals(given, result);
        assertThat(result)
                .extracting(DateRange::getFrom, DateRange::getTo)
                .containsExactly(
                        tuple(LocalDate.of(2021, 10, 10), LocalDate.of(2022, 12, 10))
                );
    }

    private List<LocalDate> generateDates(DateRange dateRange, LocalDate max) {
        LocalDate start = dateRange.getFrom();
        LocalDate end = Optional.ofNullable(dateRange.getTo()).orElse(max);
        List<LocalDate> dates = new ArrayList<>();
        while (!start.isAfter(end)) {
            dates.add(start);
            start = start.plusDays(1);
        }
        return dates;
    }

    private List<LocalDate> generateDates(DateRange dateRange) {
        return generateDates(dateRange, LocalDate.MAX);
    }

    private void assertSameIntervals(List<DateRange> given, Iterable<DateRange> result) {

        boolean containsInfinite = given.stream().anyMatch(x -> x.getTo() == null);

        var max = given.stream()
                .flatMap(x -> Stream.of(x.getFrom(), x.getTo()))
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElseThrow(RuntimeException::new);

        if (!containsInfinite) {
            assertSameIntervals(given, result, max);
        } else {
            assertSameIntervals(given, result, max.plusDays(1));
        }

    }

    private void assertSameIntervals(List<DateRange> given, Iterable<DateRange> result, LocalDate max) {
        var allDates = given.stream()
                .flatMap(x -> generateDates(x, max).stream())
                .sorted()
                .distinct()
                .toList();

        var actual = StreamSupport
                .stream(result.spliterator(), false)
                .flatMap(x -> generateDates(x, max).stream())
                .sorted()
                .distinct().toList();

        assertThat(allDates).hasSize(actual.size());
        assertThat(allDates).isEqualTo(actual);
    }

    private static void printResult(Iterable<DateRange> result) {
        result.forEach(dateRange -> System.out.printf("Date-Range: %s - %s%n", DateTimeFormatter.ISO_DATE.format(dateRange.getFrom()),
                (dateRange.getTo() != null ? DateTimeFormatter.ISO_DATE.format(dateRange.getTo()) : null)));
    }

    private static DateRange createDateRange(final LocalDate gueltigVon, final LocalDate gueltigBis) {
        return new DateRange(gueltigVon, gueltigBis);
    }
}
