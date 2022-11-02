package com.github.cobrijani.tempharm;

import java.util.List;

public class NormalSortingStrategy implements SortingStrategy<DatePoint> {
    @Override
    public void sort(List<DatePoint> data) {
        data.sort(DatePoint::compareTo);
    }
}
