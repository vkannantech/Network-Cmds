# Networks Laboratory – Model Lab Exam (12 Questions)

Solutions for the **Networks Laboratory model lab exam** (`Model_LAB Ques.pdf`). Every question has its own folder with:
- a working program,
- **Windows (`.bat`) and Linux (`.sh`) run scripts**,
- a README with Aim, Algorithm, Flow chart, real tested output, the "Explain / Discuss" answer, Viva questions and Result.

The older manual-based exercises (Ex 1–10) are kept in [`excer/`](excer/).

## Question Index
| Q | Topic | Folder | Program(s) | Ports |
|---|---|---|---|---|
| 1 | Fault isolation with ping, netstat, ifconfig, nslookup, traceroute | [Q01_Network_Troubleshooting](Q01_Network_Troubleshooting/) | `diagnose.bat` / `diagnose.sh` | – |
| 2 | Multi-user reliable **TCP file sharing** | [Q02_TCP_File_Sharing](Q02_TCP_File_Sharing/) | `FileServer`, `FileClient` | TCP 6000 |
| 3 | Reliable **UDP quiz**: ACK, retransmit, duplicate detection | [Q03_Reliable_UDP_Quiz](Q03_Reliable_UDP_Quiz/) | `QuizServer`, `QuizClient` | UDP 7000 |
| 4 | Multi-threaded **HTTP web server** with status codes | [Q04_HTTP_Web_Server](Q04_HTTP_Web_Server/) | `WebServer` + `www/` | TCP 8080 |
| 5 | Custom **DNS resolver** over UDP with cache | [Q05_DNS_Resolver_UDP](Q05_DNS_Resolver_UDP/) | `DNSServer`, `DNSClient` | UDP 5300 |
| 6 | **ARP** request/reply, cache, spoofing defence | [Q06_ARP_Cache_Spoofing](Q06_ARP_Cache_Spoofing/) | `ARPSimulator` | – |
| 7 | **RARP** server for diskless clients + DHCP-style leases | [Q07_RARP_Server](Q07_RARP_Server/) | `RARPServer`, `RARPClient` | UDP 5400 |
| 8 | **Sliding window (Go-Back-N) + CRC** over an unreliable channel | [Q08_Sliding_Window_CRC](Q08_Sliding_Window_CRC/) | `SlidingWindowCRC` | – |
| 9 | **Distance Vector** + split horizon / poison reverse | [Q09_Distance_Vector_PoisonReverse](Q09_Distance_Vector_PoisonReverse/) | `DistanceVectorSim` | – |
| 10 | **Link State (Dijkstra)** + link failure, vs DV | [Q10_Link_State_Dijkstra](Q10_Link_State_Dijkstra/) | `LinkStateSim` | – |
| 11 | **RIP vs OSPF**: convergence, PDR, overhead | [Q11_Routing_Protocol_Comparison](Q11_Routing_Protocol_Comparison/) | `RoutingSimulator` | – |
| 12 | **Packet Tracer campus network with VLANs** | [Q12_PacketTracer_VLAN_Campus](Q12_PacketTracer_VLAN_Campus/) | `configs/*.txt` + ready `.pkt` (PT 8.2.2 and PT 9.0) | – |

## Requirements
- **Java JDK 8 or newer** (`javac -version`). All code is checked to compile with `--release 8`, so it works on old college JDKs.
- Cisco Packet Tracer 8.x / 9.x (Q12, optional for Q11).
- No external libraries.

## How to Run
### Windows (college PC)
Open the question folder and **double-click** the `.bat` file. It compiles and then runs.
- Single program: `run.bat`
- Client/server: first `run_server.bat`, then `run_client.bat` (each opens its own window)
- Compile everything at once: `compile_all.bat` in this folder

### Linux (laptop)
```bash
cd Q02_TCP_File_Sharing
./run_server.sh          # terminal 1
./run_client.sh          # terminal 2
```
Compile everything: `./compile_all.sh`. Run the full automatic test suite: `./verify_all.sh`

> Always recompile (the scripts do) before running on another PC; `.class` files from a newer JDK fail on an older one with `UnsupportedClassVersionError`.

## Exam Tips
- **Start the server before the client** (Q2, Q3, Q5, Q7). Stop a server with **Ctrl + C**.
- "Address already in use" means an old server is still running. Close it or wait a few seconds.
- All ports are **above 1024**, so no admin/sudo is needed on Linux.
- Client on another PC: pass the server IP, e.g. `run_client.bat 192.168.1.5`. Allow Java through the Windows Firewall when asked.
- Q3 loss demo: `run_server.bat 0.3` and `run_client.bat localhost 0.3`.
- Q8 CRC: generator `1011` with data `1001101` gives CRC `101` (the lab manual's `100` is a typo).

## Verification (Linux, JDK 25, compiled with --release 8)
| Q | Test performed | Result |
|---|---|---|
| 1 | Full diagnosis + forced DNS failure (`nosuchsite.invalid`) | PASS – reports DNS FAILURE correctly |
| 2 | 2 clients at once: LIST, GET, missing file, PUT | PASS – CRC32 matched, interleaved threads |
| 3 | 0% and 30% simulated loss | PASS – retransmits + duplicates handled, correct score |
| 4 | 200/403/404/405/400, HEAD, 50 concurrent requests | PASS – 50/50 OK |
| 5 | Forward, reverse, cache hit, NXDOMAIN, FORMERR, server down | PASS |
| 6 | Miss/hit, unreachable, 2 spoof attacks, security off, aging | PASS |
| 7 | rarp-only and hybrid: static, dynamic, renew, exhausted, bad MAC | PASS |
| 8 | 30% and 0% error rate; manual CRC example | PASS – all frames delivered |
| 9 | Isolating failure (count-to-infinity) + reroute failure | PASS – matches manual DV tables |
| 10 | Dijkstra + link 2-4 failure + DV comparison | PASS – matches manual (0,10,50,30,60) |
| 11 | 3 scenarios × 2 protocols | PASS |
| 12 | Reference `.pkt` decoded and checked (VLANs, dot1Q, DHCP) | Configs to paste; build verified in PT by student |
