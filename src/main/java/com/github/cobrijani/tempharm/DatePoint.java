package com.github.cobrijani.tempharm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
public class DatePoint implements Comparable<DatePoint> {
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
        return "[" + (isFrom ? "E" : "A") + "] " + (date != null ? DateTimeFormatter.ISO_DATE.format(date) : "inf");
    }

    public int[] toArray() {
        int[] retVal = new int[5];
        retVal[0] = Optional.ofNullable(this.getDate()).map(this::encodeToInt).orElse(Integer.MAX_VALUE);
        retVal[1] = this.isFrom() ? 1 : 0;
        retVal[2] = Optional.ofNullable(this.getPair()).map(LocalDate::getDayOfMonth).orElse(Integer.MAX_VALUE);
        retVal[3] = Optional.ofNullable(this.getPair()).map(LocalDate::getMonthValue).orElse(Integer.MAX_VALUE);
        retVal[4] = Optional.ofNullable(this.getPair()).map(LocalDate::getYear).orElse(Integer.MAX_VALUE);
        return retVal;
    }

    private int encodeToInt(LocalDate date) {
        return Integer.parseInt(
                date.getYear() +
                        addLeadingZero(String.valueOf(date.getMonthValue())) +
                        addLeadingZero(String.valueOf(date.getDayOfMonth())));
    }

    private static LocalDate decodeFromInt(int date) {
        if (date == Integer.MAX_VALUE) {
            return null;
        }

        var stringDate = String.valueOf(date);

        return LocalDate.of(
                Integer.parseInt(stringDate.substring(0, 4)),
                Integer.parseInt(stringDate.substring(4, 6)),
                Integer.parseInt(stringDate.substring(6, 8)));
    }

    private String addLeadingZero(String number) {
        if (number.length() == 1) {
            return "0" + number;
        } else {
            return number;
        }
    }

    public static DatePoint from(int[] current) {
        return new DatePoint(decodeFromInt(current[0]), current[1] > 0, createDate(current[2], current[3], current[4]));
    }

    private static LocalDate createDate(int day, int month, int year) {
        if (day == Integer.MAX_VALUE && month == Integer.MAX_VALUE && year == Integer.MAX_VALUE) {
            return null;
        }

        if (day >= 0 && month >= 0 && year >= 0) {
            return LocalDate.of(year, month, day);
        } else {
            return null;
        }
    }
}
