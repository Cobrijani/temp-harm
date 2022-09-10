package com.github.cobrijani.tempharm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class OptimizedTemporalHarmonizerAlgorithm implements TemporalHarmonizerAlgorithm {
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
                    result.add(new DateRange(first.date, second.date));
                }
                if (endBegin(first, second)) {
                    openOrCloseInterval(intervals, second);
                    second.setFrom(!second.isFrom);
                }
                continue;
            }
            if (openInterval(intervals)) {
                appendNewRange(result, first, second);
            }
        }
        return result;
    }

    private static List<DatePoint> preparePoints(Iterable<DateRange> dateRanges) {
        return StreamSupport.stream(dateRanges.spliterator(), false)
                .flatMap(dateRange -> Stream.of(new DatePoint(dateRange.getFrom(), true, dateRange.getTo()),
                        new DatePoint(dateRange.getTo(), false, dateRange.getFrom())))
                .sorted()
                .toList();
    }

    private static void appendNewRange(List<DateRange> result, DatePoint first, DatePoint second) {
        int minusDays = second.isFrom ? 1 : 0;
        int plusDays = !first.isFrom ? 1 : 0;
        LocalDate from = Optional.ofNullable(first.date)
                .map(x -> x.plusDays(plusDays)).orElse(null);
        LocalDate to = Optional.ofNullable(second.date).map(x ->
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
        return !first.isFrom && second.isFrom;
    }

    private static boolean beginAndEnd(DatePoint first, DatePoint second) {
        return first.isFrom && !second.isFrom;
    }

    private static Boolean samePoint(DatePoint first, DatePoint second) {
        return Optional
                .ofNullable(first.date)
                .map(x -> x.equals(second.date))
                .orElse(false);
    }

    private boolean openInterval(Set<LocalDate> localDates) {
        return !localDates.isEmpty();
    }

    private void openOrCloseInterval(Set<LocalDate> intervalRegistry, DatePoint datePoint) {
        if (datePoint.isFrom()) {
            intervalRegistry.add(Optional.ofNullable(datePoint.pair).orElse(LocalDate.MAX));
        } else {
            intervalRegistry.remove(Optional.ofNullable(datePoint.date).orElse(LocalDate.MAX));
        }
    }

    @Data
    @AllArgsConstructor
    private static class DatePoint implements Comparable<DatePoint> {

        private final LocalDate date;
        private boolean isFrom;

        private final LocalDate pair;

        @Override
        public int compareTo(DatePoint o) {
            if (Objects.isNull(date) && Objects.isNull(o.date)) {
                return 0;
            }
            if (Objects.isNull(date)) {
                return 1;
            }
            if (Objects.isNull(o.date)) {
                return -1;
            }
            return date.compareTo(o.date);
        }

        @Override
        public String toString() {
            return "[" + (isFrom ? "E" : "A") + "] " +
                    (date != null ? DateTimeFormatter.ISO_DATE.format(date) : "inf");
        }
    }
}
