# Q11 – Performance Analysis of Two Routing Protocols (RIP vs OSPF)

## Question
> In a simulated enterprise network, **two routing protocols** must be tested under varying conditions such as **link failures, traffic congestion, and topology changes**. Analyze performance in terms of **convergence time, packet delivery ratio, and routing overhead**. Explain how the simulation results help determine the **most efficient protocol for scalable networks**.

## Aim
To simulate an enterprise network running **RIP** (distance vector) and **OSPF** (link state), apply a link failure, congestion and a topology change, and measure convergence time, packet delivery ratio (PDR) and routing overhead to pick the better protocol for scalable networks.

## Apparatus Required
Java JDK 8 or later (`RoutingSimulator.java`). Optionally Cisco Packet Tracer (manual verification below) or OPNET (see `excer/exc-09-opnet`).

## Network model
```
          1G        1G        1G
     R1 ------ R2 ------ R3 ------ R4
     | \        |                   |
   1G|  \ 2 Mbps WAN (slow, few hops)|1G
     |   \______________________    |
     R5 ------ R6 ------ R7 ------ R8
          1G   (R2-R6 1G cross-link) 1G
```
- 8 routers; 1 Gbps core links on two paths between R1 and R8; one **2 Mbps WAN link R1–R8** (only 25 pkt/s capacity).
- Traffic: 6 flows (R1↔R8, R2→R7, R5→R4, R3→R5, R6→R4), 10 packets/s each, 120 s simulated, event at t = 10 s.
- **RIP:** hop-count metric, periodic updates every 30 s (timers not synchronised), poison reverse.
- **OSPF:** cost = 1000 / bandwidth, hello every 10 s, LSA flooding (1 s per hop) + SPF delay 2 s.

| Scenario | Event at t = 10 s |
|---|---|
| 1. Link failure | Link R6–R7 goes down |
| 2. Traffic congestion | A backup job adds 15 pkt/s on the WAN link (load > capacity) |
| 3. Topology change | A new 1 Gbps link R4–R5 is added |

**Metrics**
- **Convergence time** = time from the event until the last routing table change.
- **PDR** = packets delivered ÷ packets sent × 100. A packet is dropped if there's no route, the link is dead, there's a loop (TTL), or the link is congested.
- **Overhead** = routing messages and bytes (RIP updates; OSPF hellos + LSAs).

## Algorithm
1. Build the topology and start both protocols converged (RIP by Bellman-Ford with hop count; OSPF by Dijkstra with bandwidth cost).
2. For each second t = 0 … 119:
   1. At t = 10, apply the scenario event. RIP: attached routers invalidate dead routes. OSPF: endpoints flood an LSA, and router r installs the new SPF at `t + hops(r) + SPF_DELAY`.
   2. RIP: routers whose 30 s timer fires send their vector (with poison reverse) to their neighbours. Count messages and bytes.
   3. OSPF: every 10 s send hellos on every link. Count them.
   4. Forward 10 packets per flow hop by hop using each router's current table. Compute link loads; drop the excess on overloaded links.
3. Record PDR, convergence time and overhead for each protocol and scenario.
4. Print the results table, the scalability estimate and the conclusion.

## How to Run
| Windows | Linux |
|---|---|
| `run.bat` | `./run.sh` |

No input is needed.

## Sample Output (verified)
```
=== Scenario 1: Link failure (R6-R7 down) ===
   RIP   flow R2->R7 : before R2-R6-R7               after R2-R1-R8-R7
   OSPF  flow R2->R7 : before R2-R6-R7               after R2-R3-R4-R8-R7
=== Scenario 2: Traffic congestion on WAN ===
   RIP   flow R1->R8 : before R1-R8                  after R1-R8
   OSPF  flow R1->R8 : before R1-R2-R3-R4-R8         after R1-R2-R3-R4-R8
=== Scenario 3: Topology change (new R4-R5) ===
   RIP   flow R3->R5 : before R3-R2-R1-R5            after R3-R4-R5
   OSPF  flow R3->R5 : before R3-R2-R1-R5            after R3-R4-R5

============================== RESULTS ==============================
Scenario                       Proto  Converge(s)  PDR(%)    Messages  Bytes
Link failure (R6-R7 down)      RIP    59           90.97     72        13824
                               OSPF   4            99.44     240       16152
Traffic congestion on WAN      RIP    n/a          91.27     80        15360
                               OSPF   n/a          100.00    240       15360
Topology change (new R4-R5)    RIP    29           100.00    86        16512
                               OSPF   4            100.00    290       19568

Average PDR : RIP 94.08 %   OSPF 99.81 %
Average convergence : RIP 44 s   OSPF 4 s

Scalability (steady-state overhead, bytes per minute, avg 3 links per router):
Routers    RIP            OSPF           RIP / OSPF
8          9216           9216           1.0x
16         33792          18432          1.8x
32         129024         36864          3.5x
64         503808         73728          6.8x
128        1990656        147456         13.5x
(RIP also cannot use paths longer than 15 hops; OSPF scales further with areas.)
Most efficient protocol for a scalable enterprise network: OSPF (link state)
```

## Analysis (answer to "Analyze / Explain")
**1. Convergence time:** OSPF 4 s vs RIP 29–59 s. OSPF floods the change immediately and each router recomputes once. RIP only spreads news on its 30 s periodic timer, one hop per update, so convergence takes multiples of 30 s.

**2. Packet delivery ratio**
- *Link failure:* RIP lost about 9% of packets because flow R2→R7 had no valid route for almost a minute (black hole). OSPF lost only 0.56% during its 4 s convergence.
- *Congestion:* RIP's hop-count metric picks the 1-hop **2 Mbps WAN** for R1↔R8. Once the backup traffic overloads it, RIP traffic is dropped (91% PDR). OSPF's bandwidth-based cost already sends R1↔R8 over the 1 Gbps core, so its PDR is 100%. (Neither protocol reacts to load dynamically; OSPF wins because of its **metric**.)
- *Topology change:* adding a link causes no loss for either protocol (the old paths still work), but OSPF starts using the shorter path R3-R4-R5 within 4 s vs 29 s for RIP.

**3. Routing overhead**
- In this small 8-router network the bytes are similar: OSPF sends more but small messages (hellos every 10 s); RIP sends fewer but large ones (the full table every 30 s).
- **Scalability:** a RIP update carries the *whole* routing table, so overhead grows with N². OSPF hellos are constant per link, and LSAs are sent only on change. At 64 routers RIP uses **6.8×** the bandwidth of OSPF, and **13.5×** at 128.
- RIP is also limited to 15 hops; OSPF uses **areas** to scale to hundreds of routers.

**How the results determine the best protocol:** the simulation measures the actual effect of each design choice. Fast convergence plus a bandwidth-aware metric gives OSPF a higher PDR in every dynamic condition, and its overhead grows linearly while RIP's grows quadratically. **OSPF is therefore the most efficient choice for scalable enterprise networks.** RIP is only acceptable for very small, stable networks because it is simple to configure.

## Verify in Cisco Packet Tracer (optional practical)
Build 4 routers in a ring (R1–R2–R3–R4–R1), each with a PC LAN, and run the same test with each protocol:
```
! RIP on every router
router rip
 version 2
 no auto-summary
 network 10.0.0.0
 network 192.168.1.0        ! (each router's own LANs)

! OSPF instead (remove RIP first: no router rip)
router ospf 1
 network 10.0.0.0 0.255.255.255 area 0
 network 192.168.1.0 0.0.0.255 area 0
```
1. From a PC run `ping -t <far PC>`, then **shut down** a link in the path (`interface g0/1`, then `shutdown`).
2. Count the lost pings. **RIP loses many** (slow convergence); **OSPF loses 1–2**.
3. Check with `show ip route`, `show ip protocols`, `show ip ospf neighbor`, `debug ip rip`.
4. In **Simulation mode**, filter RIP/OSPF packets to compare overhead.

## Viva Questions
1. **RIP vs OSPF metric?** RIP: hop count (max 15). OSPF: cost = reference bandwidth ÷ interface bandwidth.
2. **RIP timers?** Update 30 s, invalid 180 s, hold-down 180 s, flush 240 s.
3. **OSPF timers?** Hello 10 s, dead 40 s (broadcast networks).
4. **What is PDR?** Delivered packets ÷ sent packets × 100.
5. **What is convergence?** The state where all routers have consistent, correct routing tables after a change.
6. **Why is OSPF more scalable?** Hierarchical areas, LSAs only on change, no hop limit, fast SPF convergence.

## Result
RIP and OSPF were simulated under link failure, traffic congestion and topology change. OSPF converged about 10× faster (4 s vs 44 s average), delivered 99.8% vs 94.1% of packets, and its overhead scales linearly, so OSPF was determined to be the most efficient protocol for scalable enterprise networks.
