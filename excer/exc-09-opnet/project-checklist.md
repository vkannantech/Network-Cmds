# Exercise 9: OPNET Simulation Checklist

## 1. TCP/UDP Performance Analysis
- [ ] Create project `TCP_UDP_Analysis`, Scenario: `No_Drop`.
- [ ] Nodes: `Server_West`, `Client_East`, `100BaseT` links.
- [ ] Stats: `TCP Congestion Window Size`, `Sent Segment Sequence Number`.
- [ ] Scenarios: `No_Drop`, `Drop_NoFast`, `Drop_Fast` (10 min simulation).

## 2. RIP Dynamic Routing Analysis
- [ ] Create project `<StudentID>_EE2480_Lab2`, Scenario: `NO_Failure`.
- [ ] 4 Routers (`ethernet_slip8_gtwy`), `PPP_DS3` links (44.736 Mbps), 2 LANs per router.
- [ ] Scenario `Failure`: Add Failure Recovery object, cut link between Router 1 and Router 2 at $t = 200	ext{s}$.
- [ ] Stats: `RIP Traffic Sent/Received`, `Router1 Route Table Total Number of Updates`.
