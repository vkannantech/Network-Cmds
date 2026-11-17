# Q10 – Link State Routing (Dijkstra) with Link Failure, vs Distance Vector

## Question
> Simulate routers using **Link State Routing** to compute paths via **Dijkstra's algorithm**. Include **link failure scenarios** to test responsiveness. **Compare with Distance Vector** Routing in terms of **convergence speed and accuracy**.

## Aim
To compute shortest paths and routing tables with Dijkstra's SPF algorithm, simulate a link failure (LSA flooding + SPF recalculation), and compare convergence speed and accuracy against distance vector on the same network.

## Apparatus Required
Java JDK 8 or later. File: `LinkStateSim.java`.

## Topology (same as the lab manual, 0 = no link)
```
         10          50
    0 -------- 1 -------- 2
    | \                 / |
  30|  \100        20  /  |10
    |   \             /   |
    3 ---\-----------+    |
     \    \               |
      \60  +------------- 4
       +------------------+
```
Input (source 0, then fail link 2–4):
```
5
0 10 0 30 100
10 0 50 0 0
0 50 0 20 10
30 0 20 0 60
100 0 10 60 0
0
2 4
```

## Algorithm
**Dijkstra (SPF) from source s**
1. `dist[s] = 0`, all others ∞; every node unvisited.
2. Pick the unvisited node u with the smallest dist and mark it visited.
3. For each neighbour v of u: if `dist[u] + cost(u,v) < dist[v]`, update `dist[v]` and set `prev[v] = u`.
4. Repeat until every node is visited. Build each path by following `prev`; the **next hop** is the first node on the path.

**Link failure (link state reaction)**
1. Remove the link. Both endpoint routers **flood an LSA** "link down" to all routers (each router forwards it to its other neighbours once).
2. Every router now has the same updated map (link-state database) and re-runs SPF. Converged.
3. Count flooding hops and LSA messages.

**Comparison:** run synchronous distance vector (Bellman-Ford with poison reverse) on the same failure, counting rounds, messages and **wrong route entries** during convergence.

## Flow chart
```
 build link-state database (full topology)
          |
 Dijkstra SPF -> routing table (cost, next hop, path)
          |
 link fails -> endpoints flood LSA -> all routers update map
          |
 every router re-runs SPF (1 computation) -> new table
          |
 compare with DV: rounds, messages, wrong entries, final paths
```

## How to Run
| Windows | Linux |
|---|---|
| `run.bat` | `./run.sh` |

Paste the input above.

## Sample Output (verified)
```
========== BEFORE FAILURE ==========
Step  Selected  Distances [0, 1, 2, 3, 4]
1     0             0   10  inf   30  100
2     1             0   10   60   30  100
3     3             0   10   50   30   90
4     2             0   10   50   30   60
5     4             0   10   50   30   60

Routing table of router 0:
Dest  Cost   NextHop  Path
0     0      -        0
1     10     1        0 -> 1
2     50     3        0 -> 3 -> 2
3     30     3        0 -> 3
4     60     3        0 -> 3 -> 2 -> 4

***** Link 2 - 4 FAILED *****
Routers 2 and 4 flood LSAs: "link 2-4 is DOWN"
LSAs reach every router in 1 hop(s), 16 LSA messages; every router re-runs SPF

========== AFTER FAILURE (new SPF) ==========
Step  Selected  Distances [0, 1, 2, 3, 4]
1     0             0   10  inf   30  100
2     1             0   10   60   30  100
3     3             0   10   50   30   90
4     2             0   10   50   30   90
5     4             0   10   50   30   90

Routing table of router 0:
Dest  Cost   NextHop  Path
0     0      -        0
1     10     1        0 -> 1
2     50     3        0 -> 3 -> 2
3     30     3        0 -> 3
4     90     3        0 -> 3 -> 4

Changed routes from router 0:
  to 4: 60 -> 90

================ LINK STATE vs DISTANCE VECTOR ================
Metric                                 Link State     Distance Vec
Convergence (rounds)                   2              3
Control messages after failure         16             48
Wrong route entries while converging   0              4
Final shortest paths                   optimal        same (optimal)
Knowledge of topology                  full map       neighbours only
```
Before the failure, the distances `0, 10, 50, 30, 60` match the lab manual. After the failure, node 4 is rerouted via 3 (cost 90).

## Comparison (answer to "Compare")
| Aspect | Link State (OSPF) | Distance Vector (RIP) |
|---|---|---|
| Knowledge | Complete topology map at every router | Only neighbours' distance vectors ("routing by rumour") |
| Algorithm | Dijkstra SPF | Bellman-Ford |
| **Convergence speed** | **Fast**: flood the LSA once (1 hop here) + one SPF run = 2 rounds | **Slower**: needs several exchange rounds (3 here; 11 with count-to-infinity in Q9) |
| **Accuracy while converging** | **0 wrong entries**: each router computes from a consistent map | **4 wrong entries**: stale routes are used until the news propagates |
| Loops | No loops (each router knows the full graph) | Possible (count-to-infinity), needs split horizon / poison reverse |
| Final result | Optimal | Optimal (both agree after convergence) |
| Messages on change | Only the LSA about the change (16) | Full vectors from every router each round (48) |
| CPU / memory | Higher (stores the whole map, runs SPF) | Lower |
| Scalability | Large networks (OSPF areas) | Small networks (max 15 hops) |

**Responsiveness:** a link failure is detected by the two attached routers, which flood an LSA immediately. Each router recomputes with one SPF run, so the whole network switched to the new path (node 4 via 3) in about 2 rounds with no incorrect routing.

## Viva Questions
1. **Dijkstra's time complexity?** O(V²) with an array; O(E log V) with a priority queue.
2. **What is an LSA?** A Link State Advertisement: a router's description of its links and their costs, flooded to all routers.
3. **Why doesn't link state get count-to-infinity?** Every router computes paths from the same complete map, rather than trusting neighbours' distances.
4. **Does Dijkstra work with negative weights?** No. Use Bellman-Ford for negative costs.
5. **What is the OSPF cost?** Reference bandwidth ÷ interface bandwidth (default 100 Mbps ÷ bw).
6. **Example link state protocols?** OSPF and IS-IS.

## Result
Link state routing with Dijkstra's algorithm was simulated. Shortest paths and routing tables were computed, a link failure was handled with LSA flooding and SPF recomputation, and link state converged faster and without incorrect routes compared with distance vector, with both reaching the same optimal paths.
