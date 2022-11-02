package com.github.cobrijani.tempharm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LsdRadix {

    private static int getMax(int[][] arr, int n, int q) {
        int maxi = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            maxi = Math.max(maxi, arr[i][q]);
        }
        return maxi;
    }

    // A function to do counting sort of arr[]
    // according to given q i.e.
    // (0 for day, 1 for month, 2 for year)
    private static void sortDatesUtil(int[][] arr,
            int n, int q, int arrNum) {
        int maxi = getMax(arr, n, q);
        int p = 1;
        while (maxi > 0) {
            // to extract last digit  divide
            // by p then %10
            // Store count of occurrences in cnt[]
            int[] cnt = new int[10];
            for (int i = 0; i < n; i++) {
                cnt[(arr[i][q] / p) % 10]++;
            }
            for (int i = 1; i < 10; i++) {
                cnt[i] += cnt[i - 1];
            }
            int[][] ans = new int[n][arrNum];
            for (int i = n - 1; i >= 0; i--) {
                int lastDigit = (arr[i][q] / p) % 10;
                // Build the output array
                System.arraycopy(arr[i], 0, ans[cnt[lastDigit] - 1], 0, arrNum);
                cnt[lastDigit]--;
            }
            // Copy the output array to arr[],
            // so that arr[] now
            // contains sorted numbers
            // according to current digit
            for (int i = 0; i < n; i++) {
                System.arraycopy(ans[i], 0, arr[i], 0, arrNum);
            }

            // update p to get
            // the next digit
            p *= 10;
            maxi /= 10;
        }
    }

    public static void sort(List<DatePoint> dates) {
        int[][] values = from(dates);
        sortDatesUtil(values, values.length, 0, 5);

        List<DatePoint> retVal = from(values);
        dates.clear();
        dates.addAll(retVal);
    }

    private static void printArr(List<DatePoint> arr) {
        for (DatePoint datePoint : arr) {
            System.out.println(datePoint.getDate() + " ");
        }
    }

    private static int[][] from(List<DatePoint> datePoints) {
        int[][] retVal = new int[datePoints.size()][];
        for (int i = 0; i < datePoints.size(); i++) {
            retVal[i] = datePoints.get(i).toArray();
        }

        return retVal;
    }

    private static List<DatePoint> from(int[][] ints) {
        List<DatePoint> retVal = new ArrayList<>(ints.length);
        for (int[] current : ints) {
            retVal.add(DatePoint.from(current));
        }
        return retVal;
    }

    public static void main(String[] args) {
        final List<DatePoint> ranges = new ArrayList<>();
        ranges.add(new DatePoint(LocalDate.of(2014, 1, 20), false, null));
        ranges.add(new DatePoint(LocalDate.of(2010, 3, 25), false, null));
        ranges.add(new DatePoint(LocalDate.of(2000, 12, 3), false, null));
        ranges.add(new DatePoint(LocalDate.of(2000, 11, 18), false, null));
        ranges.add(new DatePoint(LocalDate.of(2015, 4, 19), false, null));
        ranges.add(new DatePoint(LocalDate.of(2005, 7, 9), false, null));
        System.out.print("\nInput Dates\n");
        printArr(ranges);
        sort(ranges);
        System.out.print("\nSorted Dates\n");
        printArr(ranges);
    }
}
