# Exercise 4: Implementation of Distance Vector Routing Protocol

## 1. Experiment Number & Title
- **Experiment No**: 4
- **Manual Title**: Implementation of Distance Vector routing protocols.
- **Course**: Networks-Commands-Deeply Engineering Suite

---

## 2. Aim
To write a Java program for implementing the **Distance Vector Routing Protocol** based on the Bellman-Ford algorithm and determine the shortest path distance matrix between all pairs of nodes in a network.

---

## 3. How to Run
- CMD: `compile_run.bat`
- PowerShell: `.\compile_run.ps1`

---

## 4. Sample Input & Output
**Input Matrix (4 nodes):**
```text
0 2 999 1
2 0 3 999
999 3 0 4
1 999 4 0
```

**Expected Routing Tables:**
- Router 1: Node 1=0, Node 2=2, Node 3=5, Node 4=1
- Router 2: Node 1=2, Node 2=0, Node 3=3, Node 4=3
- Router 3: Node 1=5, Node 2=3, Node 3=0, Node 4=4
- Router 4: Node 1=1, Node 2=3, Node 3=4, Node 4=0

---

## 5. Viva Questions & Answers (14 Questions)
1. **Algorithm used?** Bellman-Ford / dynamic programming distance vector update.
2. **Distance Vector vs Link State?** DV exchanges routing vectors with immediate neighbors; LS floods link states to entire network.
3. **Bellman-Ford equation?** $D_x(y) = \min_v \{ c(x,v) + D_v(y) \}$.
4. **Real protocol using DV?** RIP (Routing Information Protocol) and Cisco IGRP/EIGRP.
5. **What is Count-to-Infinity?** Routing loop issue where routers increment hop counts endlessly for unreachable nodes.
6. **What is Split Horizon?** Never advertise a route back out the interface it was learned from.
7. **What is Poison Reverse?** Advertise failed routes back with metric 16 (infinity) to break loops.
8. **RIP max hops?** 15 hops (16 = infinity).
9. **What does 999 represent?** Infinity / no direct physical link.
10. **Why distance to self is 0?** Traversing 0 hops incurs 0 delay/cost.
11. **When is convergence reached?** When no router receives any vector update that alters its shortest path estimates.
12. **Periodic vs Triggered updates?** Periodic = every 30s; Triggered = immediate upon topology change.
13. **Algorithm complexity?** $O(N^3)$ time, $O(N^2)$ space.
14. **Why called "routing by rumor"?** Routers rely on second-hand distance vectors rather than knowing full topology.

---

## 6. Result Statement
Thus, the Java program for implementing the Distance Vector Routing Protocol was executed and verified successfully.
