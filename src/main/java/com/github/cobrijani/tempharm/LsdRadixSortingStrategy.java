package com.github.cobrijani.tempharm;

import java.util.List;

public class LsdRadixSortingStrategy implements SortingStrategy<DatePoint> {
    @Override
    public void sort(List<DatePoint> data) {
        LsdRadix.sort(data);
    }
}
