package org.example;

import java.util.Arrays;

public class WorkStealingMain {
    public static void main(String[] args) {
        int[] array = {38, 27, 43, 3, 9, 82, 10};
        int numThreads = 4;
        Task rootTask = DAGGenerator.generateMergeSortDAG(array, 0, array.length - 1);
        WorkStealing workStealing = new WorkStealing(numThreads);
        workStealing.execute(rootTask);

        System.out.println("Sorted Array: " + Arrays.toString(array));
    }
}
