package com.github.cobrijani.tempharm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BruteForceTemporalHarmonizerAlgorithm implements TemporalHarmonizerAlgorithm {
    @Override
    public Iterable<DateRange> harmonize(Iterable<DateRange> dateRanges) {
        final Set<LocalDate> alreadyHandled = new HashSet<>();
        final var previousFrom = new AtomicReference<LocalDate>();
        final Set<LocalDate> endBoundaries = new HashSet<>();
        final var inifiniteEndAvailable = new AtomicBoolean(false);
        final var result =
                StreamSupport.stream(dateRanges.spliterator(), false)
                        .sorted() //n logn
                        .peek(dateRange -> {
                            if (dateRange.getTo() != null) {
                                endBoundaries.add(dateRange.getTo());
                            } else {
                                inifiniteEndAvailable.set(true);
                            }
                        })//n
                        .filter(dateRange -> !alreadyHandled.contains(dateRange.getFrom())) // n x 1
                        .peek(dateRange -> alreadyHandled.add(dateRange.getFrom()))
                        .flatMap(dateRange -> { // n * n * logn
                            if (previousFrom.get() == null) {
                                previousFrom.set(dateRange.getFrom());
                                return Stream.empty();
                            } else if (dateRange.getTo() != null && previousFrom.get().isAfter(dateRange.getTo())) {
                                return Stream.empty();
                            }

                            final List<DateRange> currentDateRanges = new ArrayList<>();
                            final var previousTo = new AtomicReference<>(previousFrom.getAndSet(dateRange.getFrom()));
                            endBoundaries.stream()
                                    .sorted()
                                    .filter(to -> to.isBefore(dateRange.getFrom()))
                                    .filter(to -> to.isAfter(previousTo.get()))
                                    .forEach(to -> currentDateRanges.add(new DateRange(previousTo.getAndSet(to.plusDays(1)), to)));
                            final var isLast = StreamSupport.stream(dateRanges.spliterator(), false)
                                    .noneMatch(nextDateRange -> nextDateRange.getFrom().isAfter(dateRange.getFrom()));
                            final var hasFollowingEndDates = endBoundaries.stream()
                                    .filter(endBoundary -> dateRange.getTo() == null || !endBoundary.equals(dateRange.getTo()))
                                    .anyMatch(to -> to.isAfter(previousTo.get()));
                            final var isOnlyValidOnOneDay = endBoundaries.stream()
                                    .anyMatch(to -> to.equals(previousTo.get()));

                            if (dateRange.getFrom().isAfter(previousTo.get()) && (hasFollowingEndDates || isLast || isOnlyValidOnOneDay)) {
                                currentDateRanges.add(new DateRange(previousTo.get(), dateRange.getFrom().minusDays(1)));
                            }
                            return currentDateRanges.stream();
                        })
                        .collect(Collectors.toList());
        if (inifiniteEndAvailable.get()) {
            endBoundaries.stream()
                    .sorted()
                    .filter(to -> to.isAfter(previousFrom.get()))
                    .forEach(to -> result.add(new DateRange(previousFrom.getAndSet(to.plusDays(1)), to)));
            result.add(new DateRange(previousFrom.get(), null));
        } else if (result.isEmpty()) {
            return StreamSupport
                    .stream(dateRanges.spliterator(), false)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        }
        return result.stream().sorted().collect(Collectors.toList());
    }
}
