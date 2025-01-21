package org.example;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinMergeSort extends RecursiveAction {
    private int[] array;
    private int start, end;

    public ForkJoinMergeSort(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (start < end) {
            int mid = (start + end) / 2;


            ForkJoinMergeSort leftTask = new ForkJoinMergeSort(array, start, mid);
            ForkJoinMergeSort rightTask = new ForkJoinMergeSort(array, mid + 1, end);
            invokeAll(leftTask, rightTask);
            merge(start, mid, end);
        }
    }

    private void merge(int start, int mid, int end) {
        int[] temp = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;

        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = array[i++];
        }

        while (j <= end) {
            temp[k++] = array[j++];
        }

        System.arraycopy(temp, 0, array, start, temp.length);
    }

    public static void main(String[] args) {
        int[] array = {38, 27, 43, 3, 9, 82, 10};

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinMergeSort task = new ForkJoinMergeSort(array, 0, array.length - 1);
        pool.invoke(task);
        System.out.println("Sorted Array: " + java.util.Arrays.toString(array));
    }
}
