# Q7 – RARP Server for Diskless Clients (+ DHCP-style Integration)

## Question
> A legacy network with **diskless clients** needs IP addresses via **RARP**. Implement a server to **assign IPs based on MAC addresses**. Discuss **RARP limitations** and suggest how **dynamic methods like DHCP** could be integrated.

## Aim
To implement a RARP server (over UDP) that returns an IP address for a client's MAC address from a static table. It is extended with a DHCP-style dynamic pool with leases for unknown MACs, to show how DHCP fixes RARP's limitations.

## Apparatus Required
Java JDK 8 or later. Files: `RARPServer.java`, `RARPClient.java`.

## Server data
| Static RARP table (MAC → IP) | Dynamic pool (hybrid mode) |
|---|---|
| AA:BB:CC:11:22:33 → 192.168.1.11 | 192.168.1.100 – 192.168.1.102 |
| AA:BB:CC:44:55:66 → 192.168.1.12 | lease time 120 s |
| AA:BB:CC:77:88:99 → 192.168.1.13 | |

## Modes
| Command | Behaviour |
|---|---|
| `RARPServer rarp-only` | **Pure RARP:** only MACs in the table get an IP; others get "no mapping" |
| `RARPServer` (default) | **Hybrid:** static table first, then lease from the dynamic pool (DHCP-like) |

## Algorithm
**Server**
1. Create `DatagramSocket(5400)` and load the static table and the pool.
2. Loop: receive `RARP_REQUEST|MAC`. Normalise the MAC (upper case, `-` → `:`) and validate its format (else NAK).
3. If the MAC is in the static table → `RARP_REPLY|MAC|IP|static RARP mapping`.
4. Else, if in rarp-only mode → `RARP_NAK` "no mapping".
5. Else (hybrid):
   1. Reclaim expired leases.
   2. If the MAC already has a lease → renew it (same IP).
   3. Else, if the pool is empty → NAK "pool exhausted".
   4. Else take the next IP from the pool, record the lease and its expiry, and reply.
6. Send the reply to the requester.

**Client (diskless host)**
1. Read the MAC address (the only identity a diskless machine knows at boot).
2. Send `RARP_REQUEST|MAC`. Wait 2 s, retry up to 3 times.
3. Display the assigned IP or the reason for failure.

## Flow chart
```
 Diskless client                     RARP server
 "What is my IP? MAC=X" ----------->  valid MAC? --no--> NAK invalid
                                       in static table? --yes--> REPLY static IP
                                          | no
                                       rarp-only? --yes--> NAK no mapping
                                          | no (hybrid / DHCP-style)
                                       existing lease? --yes--> REPLY same IP (renew)
                                       pool empty? --yes--> NAK exhausted
                                       allocate IP + lease --> REPLY dynamic IP
 Assigned IP <-----------------------
```

## How to Run
| | Windows | Linux |
|---|---|---|
| Pure RARP server | `run_server.bat rarp-only` | `./run_server.sh rarp-only` |
| Hybrid server | `run_server.bat` | `./run_server.sh` |
| Client | `run_client.bat` | `./run_client.sh` |

Type MAC addresses, e.g. `AA:BB:CC:44:55:66`, `11:22:33:44:55:01`, then `exit`.

## Sample Output (verified)
**Pure RARP mode**
```
RARP Server on UDP 5400  mode = RARP ONLY
Enter MAC: AA:BB:CC:44:55:66  ->  Assigned IP Address: 192.168.1.12   [static RARP mapping]
Enter MAC: 11:22:33:44:55:66  ->  No IP assigned: no mapping in RARP table (admin must add it manually)
```
**Hybrid mode (client)**
```
Enter MAC Address of diskless client (exit to quit): aa-bb-cc-11-22-33
Broadcasting RARP request: "What is my IP? My MAC is aa-bb-cc-11-22-33" (attempt 1)
Assigned IP Address: 192.168.1.11   [static RARP mapping]
Enter MAC Address ...: 11:22:33:44:55:01
Assigned IP Address: 192.168.1.100   [dynamic lease from pool (120s)]
Enter MAC Address ...: 11:22:33:44:55:02
Assigned IP Address: 192.168.1.101   [dynamic lease from pool (120s)]
Enter MAC Address ...: 11:22:33:44:55:01
Assigned IP Address: 192.168.1.100   [dynamic lease RENEWED (120s)]
Enter MAC Address ...: 11:22:33:44:55:03
Assigned IP Address: 192.168.1.102   [dynamic lease from pool (120s)]
Enter MAC Address ...: 11:22:33:44:55:04
No IP assigned: address pool exhausted
Enter MAC Address ...: ZZ:11
No IP assigned: invalid MAC format
```
**Server**
```
RARP Server on UDP 5400  mode = HYBRID (RARP + DHCP-style pool)
Request from 127.0.0.1:44788 : RARP_REQUEST|11:22:33:44:55:01
   allocated 192.168.1.100 from pool, 2 left
Reply : RARP_REPLY|11:22:33:44:55:01|192.168.1.100|dynamic lease from pool (120s)
Request from 127.0.0.1:44788 : RARP_REQUEST|11:22:33:44:55:04
Reply : RARP_NAK|11:22:33:44:55:04|address pool exhausted
```
**Server not running**
```
Broadcasting RARP request: "What is my IP? My MAC is ..." (attempt 1)
  no reply...   (attempts 2, 3)
RARP server not available - client cannot boot
```

## Discussion (answer to "Discuss")
**RARP limitations**
1. **Static, manual table:** an administrator must enter every MAC→IP pair, so a new machine gets nothing (shown in rarp-only mode).
2. **Returns only an IP address:** no subnet mask, default gateway, DNS server or boot file.
3. **Works at the data-link layer** (its own EtherType 0x8035), using broadcasts that **routers don't forward**, so you need a RARP server on every subnet.
4. **No leases:** addresses are never reclaimed, so IPs are wasted.
5. **No security:** any host can answer as the server.
6. Replaced by BOOTP (RFC 951) and then **DHCP** (RFC 2131).

**Integrating DHCP (what hybrid mode demonstrates)**
| DHCP feature | Shown here |
|---|---|
| **Dynamic pool:** no manual table needed | unknown MACs get 192.168.1.100–102 |
| **Leases** with expiry, IPs reused | 120 s lease, expired IPs returned to the pool |
| **Renewal:** same client keeps the same IP | `dynamic lease RENEWED` |
| **Reservations** for known machines | static RARP table acts as DHCP reservations |
| Pool exhaustion handling | `NAK address pool exhausted` |
| Runs over **UDP** (ports 67/68), crosses routers with a **relay agent** (`ip helper-address`) | server is a UDP service |
| Full config (mask, gateway, DNS, TFTP boot server for diskless PCs, options 66/67) | would be added to the reply |

**Migration plan:** run a DHCP server; turn the existing RARP table into **DHCP reservations** so old machines keep their IPs; give new machines dynamic leases; diskless clients boot with **PXE** (DHCP + TFTP).

## Viva Questions
1. **What does RARP do?** It finds an IP address from a known MAC address (the reverse of ARP).
2. **Who uses RARP?** Diskless workstations, which know only their NIC's MAC at boot.
3. **Why is RARP obsolete?** It gives only an IP, needs a manual table and a server per subnet. BOOTP and DHCP replaced it.
4. **DHCP's four steps?** DORA: Discover, Offer, Request, Acknowledge.
5. **What is a DHCP lease?** A time-limited loan of an IP address that must be renewed.
6. **DHCP ports?** UDP 67 (server) and 68 (client).

## Result
A RARP server was implemented that assigns IP addresses to diskless clients from their MAC addresses. RARP's limitations were shown, and DHCP-style dynamic allocation with leases and renewal was integrated successfully.
