# EXERCISE 4: IMPLEMENTATION OF DISTANCE VECTOR ROUTING PROTOCOL

---

## 🎯 Aim

To write and execute a Java program that implements the **Distance Vector Routing Protocol** (based on the Bellman-Ford / Floyd-Warshall algorithm) to determine the shortest path and minimum routing distance between all nodes/routers in a communication network.

---

## 📖 Theoretical Background & Algorithm

Distance Vector Routing is a dynamic routing algorithm in which each router maintains a table (vector) giving the best known distance to each destination and the interface or next hop to use to get there.

### 📐 Routing Metric & Update Equation:

For any intermediate router $k$, the distance from node $i$ to node $j$ is updated if passing through $k$ offers a lower cost path:

$$D_{ij} = \min(D_{ij}, D_{ik} + D_{kj})$$

### 📝 Algorithm Steps:

1. **Start** the program.
2. **Read** the total number of routers/nodes ($N$).
3. **Input** the adjacency cost matrix ($N \times N$), where direct links have their respective weights, non-adjacent links have a large value (`999` representing infinity), and diagonal elements are `0` (distance to self).
4. **Initialize** the routing distance table equal to the initial cost matrix.
5. **Iterate** over all intermediate nodes $k$ from $0$ to $N-1$:
   - For every pair of source $i$ and destination $j$:
   - If $D[i][j] > D[i][k] + D[k][j]$, update $D[i][j] = D[i][k] + D[k][j]$.
6. **Display** the final updated routing table for each individual router.
7. **Stop**.

---

## 💻 Java Source Code (`DistanceVector.java`)

```java
import java.util.Scanner;

public class DistanceVector {

    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        int nodes;

        System.out.print("Enter the number of nodes: ");
        nodes = sc.nextInt();

        int cost[][] = new int[nodes][nodes];
        int distance[][] = new int[nodes][nodes];

        System.out.println("Enter the cost matrix:");

        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                cost[i][j] = sc.nextInt();
                distance[i][j] = cost[i][j];
            }
        }

        // Distance Vector / All-pairs shortest path computation
        for (int k = 0; k < nodes; k++) {
            for (int i = 0; i < nodes; i++) {
                for (int j = 0; j < nodes; j++) {

                    if (distance[i][j] >
                        distance[i][k] + distance[k][j]) {

                        distance[i][j] =
                            distance[i][k] + distance[k][j];
                    }
                }
            }
        }

        // Displaying the final routing table for each router
        System.out.println("\nRouting Table:");

        for (int i = 0; i < nodes; i++) {

            System.out.println("\nFor Router " + (i + 1));

            for (int j = 0; j < nodes; j++) {

                System.out.println("Node " + (j + 1)
                    + " Distance = " + distance[i][j]);
            }
        }

        sc.close();
    }
}
```

---

## 🧪 Sample Input & Topology

### Input:

```text
Enter the number of nodes: 4
Enter the cost matrix:
0 2 999 1
2 0 3 999
999 3 0 4
1 999 4 0
```

### Network Topology Representation:

```
     (2)
(1)-------(2)
 |         |
 |(1)      |(3)
 |         |
(4)-------(3)
     (4)
```

---

## 📊 Live Executed Output

```text
Enter the number of nodes: 4
Enter the cost matrix:
0 2 999 1
2 0 3 999
999 3 0 4
1 999 4 0

Routing Table:

For Router 1
Node 1 Distance = 0
Node 2 Distance = 2
Node 3 Distance = 5
Node 4 Distance = 1

For Router 2
Node 1 Distance = 2
Node 2 Distance = 0
Node 3 Distance = 3
Node 4 Distance = 3

For Router 3
Node 1 Distance = 5
Node 2 Distance = 3
Node 3 Distance = 0
Node 4 Distance = 4

For Router 4
Node 1 Distance = 1
Node 2 Distance = 3
Node 3 Distance = 4
Node 4 Distance = 0
```

---

## 🛠️ How to Run in Eclipse IDE

1. **Open Eclipse**: Launch Eclipse IDE and select your workspace.
2. **Create Project**: Go to **File → New → Java Project**, name it `NetworkLab`, and click **Finish**.
3. **Create Class**: Under `NetworkLab/src`, right click **New → Class**, name it `DistanceVector`, check `public static void main`, and click **Finish**.
4. **Paste Code**: Paste the code above into [`DistanceVector.java`](file:///d:/Collage%20Meterials/SEM-5/DCCN/exc/exc-4/DistanceVector.java).
5. **Run**: Click the green **Run (▶)** button or press `Ctrl + F11`.
6. **Console Input**: Type `4`, press Enter, then paste the $4 \times 4$ cost matrix into the Console.

---

## 📌 Viva & Lab Exam Review Questions

1. **Which algorithm is the basis of Distance Vector Routing?**
   - The Bellman-Ford algorithm (or Floyd-Warshall for all-pairs shortest paths).

2. **What real-world protocol uses Distance Vector Routing?**
   - RIP (Routing Information Protocol) and IGRP (Interior Gateway Routing Protocol).

3. **What is the "Count-to-Infinity" problem?**
   - A routing loop problem in distance-vector networks where nodes continuously increment link metrics after a link failure. Solutions include **Split Horizon** and **Poison Reverse**.

4. **Why do we use `999` in the cost matrix?**
   - `999` represents an infinite cost ($\infty$), denoting that no direct physical link exists between those two routers.

---

## 🏁 Result

The Java program for the Distance Vector Routing Protocol was successfully created, compiled, and executed, and the routing tables showing minimum distances to all nodes were verified.
