package com.github.cobrijani.tempharm;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DateRange implements Comparable<DateRange> {

    private LocalDate from;
    private LocalDate to;

    public List<DatePoint> createDatePoints() {
        return List.of(new DatePoint(from, true, to),
                new DatePoint(to, false, from));
    }

    @Override
    public int compareTo(DateRange other) {
        int result = this.from.compareTo(other.from);
        if (result != 0) {
            return result;
        }

        if (this.to == null && other.to == null) {
            return 0;
        } else if (this.to == null) {
            return -1;
        } else if (other.to == null) {
            return 1;
        } else {
            return this.to.compareTo(other.to) * -1;
        }
    }
}
