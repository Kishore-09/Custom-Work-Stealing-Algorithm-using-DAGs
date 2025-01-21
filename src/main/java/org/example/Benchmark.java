package org.example;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Benchmark {

    public static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(50);
        }
        return array;
    }

    public static void main(String[] args) {

        String algorithm = args[0];
        int numThreads = Integer.parseInt(args[1]);
        int arraySize = Integer.parseInt(args[2]);
        int iterations = Integer.parseInt(args[3]);

        int[] array = generateRandomArray(arraySize);
        System.out.println("Benchmarking " + algorithm + " with array size " + arraySize + " using " + numThreads + " threads over " + iterations + " iterations.");

        if (algorithm.equalsIgnoreCase("custom")) {
            benchmarkCustomWorkStealing(array, numThreads, iterations);
        } else if (algorithm.equalsIgnoreCase("forkjoin")) {
            benchmarkForkJoin(array, numThreads, iterations);
        } else {
            System.out.println("Invalid algorithm. Use 'custom' or 'forkjoin'.");
        }
    }

    private static void benchmarkCustomWorkStealing(int[] array, int numThreads, int iterations) {
        long totalExecutionTime = 0;
        for (int i = 0; i < iterations; i++) {
            int[] arrayCopy = array.clone();

            Task rootTask = DAGGenerator.generateMergeSortDAG(arrayCopy, 0, arrayCopy.length - 1);

            WorkStealing workStealing = new WorkStealing(numThreads);

            long startTime = System.nanoTime();
            workStealing.execute(rootTask);
            long endTime = System.nanoTime();

            long executionTime = (endTime - startTime) / 1_000_000;
            totalExecutionTime += executionTime;

            System.out.println("Iteration " + (i + 1) + ": Custom Work-Stealing Execution Time: " + executionTime + " ms");
        }

        long averageExecutionTime = totalExecutionTime / iterations;
        double averageTimePerThread = (double) averageExecutionTime / numThreads;

        System.out.println("Average Custom Work-Stealing Execution Time: " + averageExecutionTime + " ms");
        System.out.println("Average Execution Time Per Thread: " + averageTimePerThread + " ms");
    }

    private static void benchmarkForkJoin(int[] array, int numThreads, int iterations) {
        long totalExecutionTime = 0;
        for (int i = 0; i < iterations; i++) {

            int[] arrayCopy = array.clone();
            ForkJoinPool pool = new ForkJoinPool(numThreads);
            ForkJoinMergeSort task = new ForkJoinMergeSort(arrayCopy, 0, arrayCopy.length - 1);

            long startTime = System.nanoTime();
            pool.invoke(task);
            long endTime = System.nanoTime();

            long executionTime = (endTime - startTime) / 1_000_000;
            totalExecutionTime += executionTime;

            System.out.println("Iteration " + (i + 1) + ": ForkJoinPool Execution Time: " + executionTime + " ms");
        }

        long averageExecutionTime = totalExecutionTime / iterations;
        double averageTimePerThread = (double) averageExecutionTime / numThreads;

        System.out.println("Average ForkJoinPool Execution Time: " + averageExecutionTime + " ms");
        System.out.println("Average Execution Time Per Thread: " + averageTimePerThread + " ms");
    }
}
