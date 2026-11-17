# Q9 – Distance Vector Routing with Split Horizon / Poison Reverse

## Question
> Simulate a network using **Distance Vector Routing** with **route updates and convergence**. Address **routing loops using split horizon or poison reverse**. Evaluate the protocol's **performance in dynamic network conditions**.

## Aim
To simulate RIP-style distance vector routing between routers: exchange route updates in rounds until convergence, fail a link, and compare **no split horizon**, **split horizon** and **poison reverse** in convergence rounds, messages and count-to-infinity.

## Apparatus Required
Java JDK 8 or later. File: `DistanceVectorSim.java`.

## Topology used (5 routers; same ring as the lab manual + R5 attached to R3)
```
        2           3
   R1 ------- R2 ------- R3 ---1--- R5
   |                      |
   1                      4
   |                      |
   R4 --------------------+
```
Input matrix (999 = no link), then the link to fail:
```
5
0 2 999 1 999
2 0 3 999 999
999 3 0 4 1
1 999 4 0 999
999 999 1 999 0
3 5
```
Use `1 4` instead of `3 5` to see a failure where an **alternative path exists** (rerouting).

## Algorithm
1. Read n and the cost matrix. Infinity = 16 (as in RIP).
2. Initialise: every router knows only its directly connected neighbours (cost, next hop).
3. **Route update round:** every router sends its distance vector to each neighbour. Router i, on receiving neighbour j's vector:
   - for each destination d: `new = cost(i,j) + D_j(d)` (capped at 16)
   - **Split horizon:** j does not advertise d to i if j's route to d goes through i.
   - **Poison reverse:** j advertises d to i with cost 16 (∞) instead.
   - Update if `new < D_i(d)`, or if j is already the next hop (accept the change even if worse).
4. Repeat rounds until no table changes → **converged**. Print rounds, messages and routing tables.
5. **Fail a link:** both endpoints set routes using that link to ∞.
6. Re-run the update rounds three times (none / split horizon / poison reverse) from the same state.
7. Compare rounds, messages and whether count-to-infinity happened.

## Flow chart
```
 init tables (neighbours only)
        |
  +-> every router sends vector to neighbours
  |     (apply split horizon / poison reverse)
  |   Bellman-Ford update:  D(i,d) = min_j [ c(i,j) + D(j,d) ]
  +-- any change? --yes
        | no
   CONVERGED -> print tables
        |
   link fails -> endpoints set routes to INF -> repeat update rounds
        |
   compare: rounds, messages, count-to-infinity
```

## How to Run
| Windows | Linux |
|---|---|
| `run.bat` | `./run.sh` |

Paste the input above.

## Sample Output (verified)
**Phase 1: route updates and convergence**
```
===== PHASE 1: Initial route updates (Poison Reverse) =====
Round 1:
  R1: to R3 cost 5 via R2
  R2: to R4 cost 3 via R1
  R2: to R5 cost 4 via R3
  R3: to R1 cost 5 via R2
  R4: to R2 cost 3 via R1
  R4: to R5 cost 5 via R3
  R5: to R2 cost 4 via R3
  R5: to R4 cost 5 via R3
Round 2:
  R1: to R5 cost 6 via R2
  R5: to R1 cost 6 via R3
Converged after 2 rounds, 30 update messages

Routing table of Router 1        Routing table of Router 3
Dest   Cost   NextHop            Dest   Cost   NextHop
R1     0      -                  R1     5      R2
R2     2      R2                 R2     3      R2
R3     5      R2                 R3     0      -
R4     1      R4                 R4     4      R4
R5     6      R2                 R5     1      R5
```
(For routers 1–4 these costs match the lab manual's distance vector output: Router 1 = 0, 2, 5, 1.)

**Phase 2: link R3–R5 fails (R5 becomes unreachable)**

*No split horizon: count-to-infinity*
```
Round 1:  R2: to R5 cost INF   R3: to R5 cost 7 via R2   R4: to R5 cost INF
Round 2:  R1: INF   R2: to R5 cost 8 via R1   R3: INF   R4: to R5 cost 7 via R1
Round 3:  R1: to R5 cost 8 via R4   R3: to R5 cost 11 via R2 ...
Round 5:  R1: to R5 cost 10 via R4   R3: to R5 cost 13 via R2 ...
Round 7:  R1: to R5 cost 12 via R4   R3: to R5 cost 15 via R2 ...
...
Round 11: R4: to R5 cost INF via -
```
*Poison reverse*
```
Round 1:  R2: to R5 cost INF via -    R4: to R5 cost INF via -
Round 2:  R1: to R5 cost INF via -    R4: to R5 cost 7 via R1
Round 3:  R3: to R5 cost 11 via R4    R4: to R5 cost INF via -
Round 4:  R2: to R5 cost 14 via R3    R3: to R5 cost INF via -
Round 5:  R2: to R5 cost INF via -
```
**Performance comparison**
```
================ PERFORMANCE COMPARISON (after failure) ================
Technique          Rounds   Messages   Count-to-infinity?
No Split Horizon   11       96         YES - costs keep climbing
Split Horizon      5        48         No - only short transient
Poison Reverse     5        48         No - only short transient
```
**Reroute case (fail 1 4):** all three techniques converge in 2 rounds; R1→R4 becomes cost 9 via R2 (R1-R2-R3-R4).

## Evaluation (answer to "Evaluate")
- **Convergence:** the initial tables converged in 2 rounds (network diameter). When a cheaper or alternative path exists (*good news*), DV converges quickly: 2 rounds in the reroute case.
- **Bad news travels slowly:** when R5 became unreachable, without loop prevention the routers kept learning stale routes from each other (R3 ↔ R2 ↔ R1 ↔ R4). Costs climbed 7 → 9 → … → 15 until reaching 16 (∞): the **count-to-infinity problem**. It took 11 rounds and 96 messages, and packets looped the whole time.
- **Split horizon:** never tell a neighbour about a route learned from that neighbour. This removes 2-node loops (the classic A↔B ping-pong).
- **Poison reverse:** tell that neighbour the route is ∞, which kills the loop *actively*. Both cut convergence to 5 rounds and messages by **50%**.
- **Limitation:** in rings of 3 or more routers, split horizon / poison reverse can still allow a brief loop (the transient 7 → 11 → 14 above). Real RIP adds **triggered updates**, **hold-down timers** and the **max metric 16** to finish the job.
- **Dynamic conditions:** DV is simple and needs little memory or CPU, but convergence time grows with network size and loops are possible. That is why RIP is limited to 15 hops and used in small networks.

## Viva Questions
1. **Which algorithm does DV use?** Bellman-Ford: D(x,y) = min over neighbours v of { c(x,v) + D(v,y) }.
2. **What is count-to-infinity?** After a route fails, routers keep increasing the cost by learning it from each other until it reaches infinity.
3. **Split horizon vs poison reverse?** Split horizon omits the route back to the neighbour it was learned from. Poison reverse advertises it with an infinite metric.
4. **Why is infinity 16 in RIP?** It caps count-to-infinity. RIP networks can therefore be at most 15 hops wide.
5. **What are triggered updates?** Sending an update immediately when a route changes, instead of waiting for the 30 s periodic timer.
6. **Example DV protocols?** RIP, IGRP (and BGP is a path-vector protocol).

## Result
Distance vector routing was simulated with route updates and convergence. A link failure caused count-to-infinity without loop prevention (11 rounds), while split horizon and poison reverse removed it and halved convergence time and routing messages.
