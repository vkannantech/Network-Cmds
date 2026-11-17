# Q1 – Network Fault Isolation Using Diagnostic Commands

## Question
> In a new office, employees face issues accessing internal services. Using tools like **ping, netstat, ifconfig, nslookup and traceroute**, identify the source of the problem — whether it's **IP configuration, DNS failure, or routing issues**. Explain how each command helps in isolating and resolving the fault.

## Aim
To identify the faulty network layer (IP configuration, LAN/gateway, routing, DNS or the service itself) using standard diagnostic commands, applied in a layered (bottom-up) order.

## Apparatus Required
PC with Windows or Linux, network connection, terminal. Scripts: `diagnose.bat` (Windows) and `diagnose.sh` (Linux).

## Commands: Windows vs Linux
| Purpose | Windows | Linux |
|---|---|---|
| IP address, mask, gateway, DNS | `ipconfig /all` | `ifconfig` / `ip addr`, `ip route`, `cat /etc/resolv.conf` |
| Renew DHCP | `ipconfig /release` + `ipconfig /renew` | `sudo dhclient -r && sudo dhclient` |
| Reachability | `ping 192.168.1.1` | `ping -c 4 192.168.1.1` |
| Path / where packets stop | `tracert 8.8.8.8` | `traceroute 8.8.8.8` or `tracepath 8.8.8.8` |
| Name resolution | `nslookup www.google.com` | `nslookup www.google.com` / `dig` |
| Open connections, listening ports | `netstat -an` | `netstat -tuln` / `ss -tuln` |
| Routing table | `netstat -r` / `route print` | `netstat -rn` / `ip route` |
| ARP table | `arp -a` | `arp -a` / `ip neigh` |

## Algorithm (layered troubleshooting)
1. Start.
2. **ifconfig/ipconfig** – check the host has a valid IP, mask, gateway and DNS. If there is no IP or it is `169.254.x.x` (APIPA), the fault is **IP configuration / DHCP**.
3. **ping 127.0.0.1** – if this fails, the TCP/IP stack itself is broken.
4. **ping <default gateway>** – if this fails, the fault is the **local LAN** (cable, switch, wrong subnet mask or gateway).
5. **ping 8.8.8.8** (a public IP, no DNS used) – if this fails, it is a **routing** problem. Run **traceroute**; the fault lies just after the last hop that replies.
6. **nslookup <name>** – if ping by IP works but this fails, it is a **DNS failure**.
7. **netstat** – if everything above works but the service does not, check the server is **LISTENING** on its port (service down or firewall).
8. Report the first failing step as the faulty layer. Stop.

## Flow chart
```
 Start
   |
 ifconfig ── no IP / 169.254.x.x ──> IP CONFIGURATION FAULT (DHCP)
   | ok
 ping gateway ── fail ──> LAN FAULT (cable/switch/mask/gateway)
   | ok
 ping 8.8.8.8 ── fail ──> ROUTING FAULT (traceroute shows where)
   | ok
 nslookup name ── fail ──> DNS FAULT
   | ok
 netstat: service listening? ── no ──> SERVICE / FIREWALL FAULT
   | yes
 Network OK
```

## How to Run
**Windows (college PC):** double-click `diagnose.bat`, or in cmd: `diagnose.bat www.google.com`

**Linux (laptop):**
```bash
chmod +x diagnose.sh
./diagnose.sh                 # tests www.google.com
./diagnose.sh www.college.edu # test a specific name
```
To demonstrate a **DNS failure**, try `./diagnose.sh nosuchsite.invalid`. Steps 1–4 pass and step 5 fails, so the script reports DNS failure.

## Sample Output (Linux, verified)
```
 STEP 1 : IP configuration  (ifconfig / ip addr)
   [PASS] Host has IPv4 address 10.182.115.242
 STEP 2 : Loopback test  (ping 127.0.0.1) - is TCP/IP stack working?
   [PASS] TCP/IP stack OK
 STEP 3 : Default gateway  (ip route + ping gateway)
   Default gateway = 10.182.115.95
   [PASS] Gateway 10.182.115.95 reachable
 STEP 4 : Internet by IP  (ping 8.8.8.8) - bypasses DNS
   3 packets transmitted, 3 received, 0% packet loss
   [PASS] Remote network reachable by IP
 STEP 5 : DNS resolution  (nslookup www.google.com)
   [PASS] DNS resolves www.google.com
 STEP 6 : Path to target  (traceroute / tracepath www.google.com)
 1:  2409:40f4:10f0:df66::e2      0.771ms
 3:  2405:200:5218:20:3925::ff08 37.087ms
 ...
 STEP 7 : Connections & listening services  (netstat)
tcp   0  0 127.0.0.1:3306   0.0.0.0:*   LISTEN
Kernel IP routing table
0.0.0.0   10.182.115.95   0.0.0.0   UG   0 0   0 enx7689d4e365d7

>>> DIAGNOSIS: All layers OK - IP config, gateway, routing and DNS are working.
```

## How each command helps (answer to "Explain")
| Command | What it tells you | Fault it isolates | How to resolve |
|---|---|---|---|
| **ifconfig / ipconfig** | IP, subnet mask, gateway, DNS server, interface up/down | **IP configuration** – no IP, APIPA 169.254.x.x, wrong mask or gateway | Fix DHCP (`ipconfig /renew`), set correct static IP/mask/gateway |
| **ping** | Whether a host replies (ICMP Echo), round-trip time, packet loss | Ping loopback → stack; gateway → LAN; public IP → routing | Check cable/switch, correct gateway, contact ISP |
| **traceroute / tracert** | Every router (hop) on the path, using TTL expiry | **Routing** – the hop where replies stop is where the fault is | Fix the route/link at that hop; add missing static or default route |
| **nslookup** | Which DNS server answered and the IP for a name | **DNS failure** – ping by IP works, but names fail | Correct DNS server in DHCP/resolv.conf; add the missing DNS record |
| **netstat** | Active connections, listening ports, routing table (`-r`) | **Service** not listening, wrong port, missing routes | Start the service, open the firewall port, add the route |

**Key idea:** test bottom-up, from the host's own config, to the local network, to remote routing, to name resolution, to the application. The **first step that fails is the faulty layer**, so you don't waste time on the layers above it.

## Typical fault cases (for viva)
| Symptom | Diagnosis |
|---|---|
| `ipconfig` shows 169.254.x.x | DHCP server unreachable – IP configuration fault |
| Can ping gateway, not 8.8.8.8 | Routing / ISP fault – traceroute shows last good hop |
| Can ping 8.8.8.8, cannot open www.google.com | DNS failure |
| Can resolve and ping server, but app fails | Service not running / firewall – check `netstat -an` |

## Viva Questions
1. **Why ping 8.8.8.8 before nslookup?** Pinging an IP skips DNS. If it works but names don't resolve, the fault must be DNS.
2. **How does traceroute work?** It sends packets with TTL = 1, 2, 3, …; each router that drops a packet (TTL = 0) replies with ICMP *Time Exceeded*, which reveals that hop.
3. **What is 169.254.x.x?** An APIPA address, self-assigned when no DHCP server answers.
4. **Which protocol does ping use?** ICMP Echo Request / Echo Reply.
5. **What does `netstat -an` show?** All connections and listening ports, numerically (no name lookups).
6. **Difference between ifconfig and ipconfig?** ifconfig is Linux/Unix, ipconfig is Windows. Both show interface IP settings.

## Result
The network fault was isolated layer by layer using ifconfig/ipconfig, ping, traceroute, nslookup and netstat, and the faulty layer (IP configuration, routing or DNS) was identified successfully.
