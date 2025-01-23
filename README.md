# Custom Work-Stealing Algorithm Using DAGs

This project implements a **custom work-stealing algorithm** for task scheduling and parallelism using **Directed Acyclic Graphs (DAGs)**. The algorithm is applied to **parallel merge sort**, where worker threads dynamically steal tasks to maximize parallelism and efficiency.

---

## Project Description

### **What is Work-Stealing?**
Work-stealing is a dynamic scheduling algorithm where idle worker threads "steal" tasks from other busy threads' queues. It ensures better load balancing and minimizes idle time in parallel computing.

### **Key Features:**
1. **Custom DAG-Based Scheduler:**
   - Tasks and their dependencies are modeled as a Directed Acyclic Graph (DAG).
   - Tasks are scheduled dynamically based on their readiness (i.e., no unfulfilled dependencies).
   
2. **Parallel Merge Sort:**
   - The DAG generates tasks and dependencies for sorting subarrays.
   - Worker threads dynamically pick and execute tasks, leveraging parallelism.

3. **Benchmarking:**
   - The custom work-stealing algorithm is benchmarked against Java's inbuilt **Fork/Join Pool**.
   - Performance metrics such as execution time and scalability are compared.

---

## Technologies Used
- **Language**: Java
- **Build Tool**: Gradle
- **Framework**: Fork/Join Framework (for benchmarking)

---

## Key Components
1. **DAG Task Generation**:
   - Tasks and their dependencies are represented as nodes and edges in a DAG.
   - Ensures proper sequencing of tasks for merge sort.

2. **Custom Work-Stealing Algorithm**:
   - Worker threads dynamically fetch tasks from their own or other threads' queues.
   - Balances workload across threads to maximize CPU utilization.

3. **Parallel Merge Sort**:
   - Divides the array into smaller subarrays, sorting them in parallel.
   - Merges sorted subarrays recursively using the DAG.

4. **Benchmarking**:
   - Compared with the Java Fork/Join Pool to evaluate:
     - Execution time
     - Thread utilization
     - Scalability with increasing input size

---

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/YourUsername/Work-Stealing-DAG-MergeSort.git
