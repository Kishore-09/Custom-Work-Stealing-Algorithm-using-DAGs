package org.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

public class WorkStealing {
    private List<Deque<Task>> taskQueues;
    private int numThreads;

    public WorkStealing(int numThreads) {
        this.numThreads = numThreads;
        taskQueues = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            taskQueues.add(new ArrayDeque<>());
        }
    }

    public void execute(Task rootTask) {
        taskQueues.get(0).add(rootTask);
        for (int i = 1; i < numThreads; i++) {
            if (!rootTask.dependencies.isEmpty()) {
                taskQueues.get(i).add(rootTask.dependencies.get(i % rootTask.dependencies.size()));
            }
        }

        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            int threadId = i;
            Thread worker = new Thread(() -> runWorker(threadId));
            workers.add(worker);
            worker.start();
        }

        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void runWorker(int threadId) {
        Deque<Task> queue = taskQueues.get(threadId);
        int idleChecks = 0;

        while (true) {
            Task task = queue.pollFirst();

            if (task == null) {
                task = stealTask(threadId);
                idleChecks++;
            }

            if (task == null) {
                if (idleChecks > 10) {
                    break;
                }
                continue;
            }

            idleChecks = 0;


            if (task.isCompleted) {
                continue;
            }

            //System.out.println("Thread " + threadId + " is processing Task " + task.id);
            processTask(task, threadId);
        }
    }

    private Task stealTask(int threadId) {
        for (int i = 0; i < numThreads; i++) {
            if (i != threadId) {
                Deque<Task> victimQueue = taskQueues.get(i);
                synchronized (victimQueue) {
                    if (!victimQueue.isEmpty()) {
                        Task stolen = victimQueue.pollLast();
                        if (stolen != null) {
                            //System.out.println("Thread " + threadId + " stole Task " + stolen.id + " from Thread " + i);
                            return stolen;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void processTask(Task task, int threadId) {
        for (Task dependency : task.dependencies) {
            if (!dependency.isCompleted) {
                //System.out.println("Task " + task.id + " re-queued due to incomplete dependency " + dependency.id);

                taskQueues.get(threadId).add(dependency);
                taskQueues.get(threadId).add(task);
                return;
            }
        }

        if (task.start < task.end) {
            int mid = (task.start + task.end) / 2;
            merge(task.array, task.start, mid, task.end);
        }

        task.isCompleted = true;

        //System.out.println("Thread " + threadId + " processed Task " + task.id);
    }

    private void merge(int[] array, int start, int mid, int end) {
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
}