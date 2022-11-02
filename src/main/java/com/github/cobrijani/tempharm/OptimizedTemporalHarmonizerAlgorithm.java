package com.github.cobrijani.tempharm;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class OptimizedTemporalHarmonizerAlgorithm implements TemporalHarmonizerAlgorithm {

    private final SortingStrategy<DatePoint> sortingStrategy;

    @Override
    public Iterable<DateRange> harmonize(Iterable<DateRange> dateRanges) {
        var datePoints = preparePoints(dateRanges);
        List<DateRange> result = new ArrayList<>();
        Set<LocalDate> intervals = new HashSet<>();

        for (int i = 0; i + 1 < datePoints.size(); i++) {
            var first = datePoints.get(i);
            var second = datePoints.get(i + 1);

            openOrCloseInterval(intervals, first);

            if (samePoint(first, second)) {
                if (beginAndEnd(first, second)) {
                    result.add(new DateRange(first.getDate(), second.getDate()));
                }
                if (endBegin(first, second)) {
                    openOrCloseInterval(intervals, second);
                    second.setFrom(!second.isFrom());
                }
                continue;
            }
            if (openInterval(intervals)) {
                appendNewRange(result, first, second);
            }
        }
        return result;
    }

    private List<DatePoint> preparePoints(Iterable<DateRange> dateRanges) {
        final List<DatePoint> datePoints = StreamSupport.stream(dateRanges.spliterator(), false)
                .flatMap(dateRange -> dateRange.createDatePoints().stream())
                .collect(Collectors.toList());
        this.sortingStrategy.sort(datePoints);
        return datePoints;
    }

    private static void appendNewRange(List<DateRange> result, DatePoint first, DatePoint second) {
        int minusDays = second.isFrom() ? 1 : 0;
        int plusDays = !first.isFrom() ? 1 : 0;
        LocalDate from = Optional.ofNullable(first.getDate())
                .map(x -> x.plusDays(plusDays)).orElse(null);
        LocalDate to = Optional.ofNullable(second.getDate()).map(x ->
                x.minusDays(minusDays)).orElse(null);

        var isAfter = Optional.ofNullable(from)
                .filter(x -> Objects.nonNull(to))
                .map(x -> x.isAfter(to))
                .orElse(false);

        if (!isAfter) {
            result.add(new DateRange(
                    from, to));
        }
    }

    private static boolean endBegin(DatePoint first, DatePoint second) {
        return !first.isFrom() && second.isFrom();
    }

    private static boolean beginAndEnd(DatePoint first, DatePoint second) {
        return first.isFrom() && !second.isFrom();
    }

    private static Boolean samePoint(DatePoint first, DatePoint second) {
        return Optional
                .ofNullable(first.getDate())
                .map(x -> x.equals(second.getDate()))
                .orElse(false);
    }

    private boolean openInterval(Set<LocalDate> localDates) {
        return !localDates.isEmpty();
    }

    private void openOrCloseInterval(Set<LocalDate> intervalRegistry, DatePoint datePoint) {
        if (datePoint.isFrom()) {
            intervalRegistry.add(Optional.ofNullable(datePoint.getPair()).orElse(LocalDate.MAX));
        } else {
            intervalRegistry.remove(Optional.ofNullable(datePoint.getDate()).orElse(LocalDate.MAX));
        }
    }

}
