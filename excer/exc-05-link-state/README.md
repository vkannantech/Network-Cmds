# Exercise 5: Configure a Network Using Link State Routing Protocol

## 1. Experiment Number & Title
- **Experiment No**: 5
- **Manual Title**: Configure a network using Link state routing protocols.
- **Course**: Networks-Commands-Deeply Engineering Suite

---

## 2. Aim
To write a Java program for implementing the **Link State Routing Protocol** using Dijkstra's Single-Source Shortest Path algorithm and calculate the minimal cost path from a source router to every other destination node.

---

## 3. How to Run
- CMD: `compile_run.bat`
- PowerShell: `.\compile_run.ps1`

---

## 4. Sample Input & Output
**Input Matrix (5 nodes):**
```text
0 10 0 30 100
10 0 50 0 0
0 50 0 20 10
30 0 20 0 60
100 0 10 60 0
```
- Source Node: `0`
- **Output**:
  - To Node 0 Distance = 0
  - To Node 1 Distance = 10
  - To Node 2 Distance = 50
  - To Node 3 Distance = 30
  - To Node 4 Distance = 60

---

## 5. Viva Questions & Answers (14 Questions)
1. **Algorithm used?** Dijkstra's Shortest Path First (SPF) algorithm.
2. **Real-world Link State protocols?** OSPF (Open Shortest Path First) and IS-IS.
3. **What is an LSA?** Link State Advertisement flooded to all routers describing local link states.
4. **What is an LSDB?** Link State Database synchronized across all routers containing complete topology map.
5. **How does Link State prevent loops?** Complete identical map lets each router build loop-free Shortest Path Tree.
6. **Algorithm complexity?** $O(N^2)$ with matrix scan, $O((V+E)\log V)$ with min-heap.
7. **What is relaxation?** Checking if $	ext{dist}[u] + 	ext{weight}(u,v) < 	ext{dist}[v]$ improves shortest path to $v$.
8. **Why Dijkstra fails with negative weights?** Greedy selection assumes distances are monotonic non-decreasing.
9. **What is OSPF Area 0?** Backbone Area connecting all other areas in hierarchical OSPF.
10. **What is DR / BDR?** Designated Router & Backup Designated Router reduce multi-access LSA flooding.
11. **Why more memory/CPU for Link State?** Stores full LSDB and computes SPF tree upon changes.
12. **OSPF default cost formula?** $	ext{Cost} = 10^8 / 	ext{Bandwidth (bps)}$.
13. **Dijkstra classification?** Greedy algorithm.
14. **Why Node 4 distance is 60?** Multi-hop path $0 	o 3 	o 2 	o 4$ ($30+20+10=60$) is cheaper than direct 100.

---

## 6. Result Statement
Thus, the Java program for implementing the Link State Routing Protocol was executed and verified successfully.
