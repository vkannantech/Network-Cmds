# Exercise 1: Demonstrate Various Network Commands

## 1. Experiment Number & Title
- **Experiment No**: 1
- **Manual Title**: Demonstrate various network commands.
- **Course**: 24IT5611 – Networks Laboratory (Academic Year 2026–2027 Odd)

---

## 2. Aim
To learn and demonstrate the use of essential network diagnostic and administration commands including `ping`, `netstat`, `ipconfig`, `nslookup`, `traceroute` (`tracert`), `arp`, `hostname`, `systeminfo`, `getmac`, and `pathping` in a Windows environment.

---

## 3. Requirements & Software
- **Operating System**: Windows 10 / Windows 11
- **Shell / Terminal**: Windows Command Prompt (`cmd.exe`) or PowerShell
- **Privileges**: Standard user (elevated Administrator prompt recommended for full ARP cache and interface statistics)
- **Network Interface**: Active Ethernet / Wi-Fi adapter or Loopback adapter

---

## 4. Concepts Covered
- **ICMP Protocol (Internet Control Message Protocol)**: Echo Request (Type 8) and Echo Reply (Type 0) used in `ping` and `tracert`.
- **IP Addressing & Adapter Configuration**: IPv4, IPv6, Subnet Masks, Default Gateways, DHCP Lease times, and DNS Server assignments (`ipconfig`).
- **Transport Layer Sockets & States**: TCP state machine (`LISTENING`, `ESTABLISHED`, `TIME_WAIT`, `CLOSE_WAIT`) and UDP endpoints (`netstat`).
- **Domain Name Resolution (DNS)**: Forward queries (A/AAAA records) and reverse queries (PTR records) via `nslookup`.
- **Packet Route Path Analysis & TTL Expiration**: Time-to-Live (TTL) field decrements at router hops with ICMP Time Exceeded responses (`tracert`, `pathping`).
- **Layer-2 / Layer-3 Address Binding**: Hardware Media Access Control (MAC) mapping to IP addresses via the local ARP cache (`arp`, `getmac`).
- **Host & System Identification**: NetBIOS/DNS hostname and complete hardware/OS build specifications (`hostname`, `systeminfo`).

---

## 5. Manual-Derived Algorithm / Procedure
1. Open Windows Command Prompt (`cmd.exe`) or PowerShell.
2. **Ping**: Execute `ping 127.0.0.1` to verify local TCP/IP protocol stack functionality, then `ping 8.8.8.8` or domain name to test end-to-end network reachability and measure round-trip time (RTT).
3. **Netstat**: Run `netstat -an` to display all active TCP/UDP connections, listening ports, and numerical addresses.
4. **Ipconfig**: Execute `ipconfig /all` to display detailed adapter configurations (IPv4/IPv6 address, subnet mask, default gateway, MAC physical address, DHCP/DNS servers).
5. **Nslookup**: Execute `nslookup google.com` to query DNS infrastructure and resolve hostnames to IP addresses.
6. **Tracert (Traceroute)**: Execute `tracert 8.8.8.8` to list every intermediate routing hop and measure per-hop latency.
7. **ARP**: Run `arp -a` to view the local Address Resolution Protocol mapping table (IP-to-MAC address pairs).
8. **Hostname**: Run `hostname` to print the computer's network name.
9. **Systeminfo**: Run `systeminfo` to inspect OS configuration, security patches, RAM, BIOS version, and network card details.
10. **Getmac**: Run `getmac` to view physical MAC addresses of all network adapters.
11. **Pathping**: Run `pathping 8.8.8.8` to combine ping and traceroute metrics with packet loss statistics per router hop.

---

## 6. Files in this Exercise
- [run_commands.bat](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-01-network-commands/run_commands.bat): Automated Windows batch runner that executes all 10 diagnostic commands sequentially with formatted headers.
- [run_commands.ps1](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-01-network-commands/run_commands.ps1): PowerShell runner script with formatted headers.
- [README.md](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-01-network-commands/README.md): Comprehensive documentation, command syntax, viva Q&As, and lab record guide.

---

## 7. How to Run on Windows CMD / PowerShell

### Option A: CMD Batch Runner
```cmd
cd network-lab\exc-01-network-commands
run_commands.bat
```

### Option B: PowerShell Runner
```powershell
cd network-lab\exc-01-network-commands
.un_commands.ps1
```

---

## 8. Sample Input
- Local loopback IP: `127.0.0.1`
- Remote target IP: `8.8.8.8` (Google Public DNS)
- Target domain name: `google.com`

---

## 9. Expected Output / Verification Criteria

```text
===============================================================================
       24IT5611 - NETWORKS LABORATORY - EXERCISE 1
       DEMONSTRATE VARIOUS NETWORK COMMANDS
===============================================================================

[1/10] Testing Reachability with PING...
Pinging 127.0.0.1 with 32 bytes of data:
Reply from 127.0.0.1: bytes=32 time<1ms TTL=128
Reply from 127.0.0.1: bytes=32 time<1ms TTL=128

[2/10] Inspecting Network Statistics with NETSTAT...
Active Connections:
  Proto  Local Address          Foreign Address        State
  TCP    0.0.0.0:135            0.0.0.0:0              LISTENING
  TCP    192.168.1.10:52310     142.250.190.46:443     ESTABLISHED

[3/10] Viewing Network Configuration with IPCONFIG...
Windows IP Configuration
   Host Name . . . . . . . . . . . . : LAB-PC
   IPv4 Address. . . . . . . . . . . : 192.168.1.100
   Subnet Mask . . . . . . . . . . . : 255.255.255.0
   Default Gateway . . . . . . . . . : 192.168.1.1

[4/10] Querying DNS Infrastructure with NSLOOKUP...
Non-authoritative answer:
Name:    google.com
Addresses:  142.250.195.206

[5/10] Tracing Packet Route with TRACERT...
  1    <1 ms    <1 ms    <1 ms  192.168.1.1
  2     2 ms     2 ms     2 ms  10.20.0.1
  3    12 ms    11 ms    12 ms  8.8.8.8

[6/10] Inspecting Address Resolution Protocol Table with ARP...
Interface: 192.168.1.100 --- 0x10
  Internet Address      Physical Address      Type
  192.168.1.1           a0-04-60-xx-xx-xx     dynamic

[7/10] Getting Host Name with HOSTNAME...
LAB-PC

[8/10] Displaying System Configuration with SYSTEMINFO...
OS Name:                   Microsoft Windows 11 Pro
OS Version:                10.0.22631 N/A Build 22631

[9/10] Displaying Physical MAC Addresses with GETMAC...
Physical Address    Transport Name
=================== ====================================================
00-15-5D-12-34-56   \Device\Tcpip_{...}

[10/10] Measuring Latency and Packet Loss with PATHPING...
Computing statistics for 75 seconds...
            Source to Here   This Node/Link
Hop  RTT    Lost/Sent = Pct  Lost/Sent = Pct  Address
  0                                           LAB-PC [192.168.1.100]
                                0/ 100 =  0%   |
  1    1ms     0/ 100 =  0%     0/ 100 =  0%  192.168.1.1
```

---

## 10. Explanation of Important Commands & Switches
1. **`ping -n <count>`**: Controls how many Echo Request packets are sent. `-t` pings continuously until interrupted (Ctrl+C).
2. **`netstat -a -n -o`**: `-a` displays all active connections and listening ports; `-n` displays numerical IP:Port pairs; `-o` shows the owning PID.
3. **`ipconfig /all` vs `/release` vs `/renew`**: `/all` dumps all adapter details; `/release` releases the DHCP lease; `/renew` requests a fresh lease.
4. **`nslookup <host> [dns_server]`**: Tests resolution against specific DNS nameservers.
5. **`tracert -d -h <max_hops>`**: `-d` prevents resolving IP addresses to hostnames to accelerate execution; `-h` limits maximum hops.
6. **`arp -a` vs `arp -d`**: `-a` displays the cached ARP table; `-d` deletes the dynamic entries.
7. **`pathping`**: Maps the hop path and sends repeated queries over a measurement interval to compute link packet loss.

---

## 11. Common Errors and Fixes
- **Error: "Request timed out" on Ping**: Target host unreachable or firewall blocking ICMP. Check physical connection and gateway.
- **Error: "traceroute is not recognized"**: Unix command entered on Windows. Use Windows command `tracert`.
- **Error: "ifconfig is not recognized"**: Linux command entered on Windows. Use Windows command `ipconfig`.

---

## 12. Viva Questions and Concise Answers

1. **What is the primary function of the `ping` utility?**
   - *Answer*: `ping` tests network reachability between source and destination hosts and measures Round-Trip Time (RTT) using ICMP Echo Request and Echo Reply messages.
2. **Which transport-layer protocol does `ping` use?**
   - *Answer*: `ping` does not use TCP or UDP; it operates directly above IP at Layer 3 using ICMP (Internet Control Message Protocol, IP protocol number 1).
3. **What is the difference between `ipconfig` and `ifconfig`?**
   - *Answer*: `ipconfig` is the native network configuration tool for Windows operating systems, whereas `ifconfig` (and modern `ip addr`) is used on Unix/Linux systems.
4. **What information does `netstat -an` provide?**
   - *Answer*: It lists all active TCP/UDP connections and listening sockets with numerical IP addresses and port numbers without attempting domain name resolution.
5. **How does `tracert` determine intermediate router hops?**
   - *Answer*: It sends packets with incrementally increasing Time-To-Live (TTL) values starting from 1. Each intermediate router decrements TTL, drops the packet when TTL=0, and returns an ICMP Time Exceeded (Type 11) message.
6. **What is the purpose of the ARP protocol and what does `arp -a` show?**
   - *Answer*: ARP resolves Layer-3 IPv4 addresses to Layer-2 physical MAC addresses on a local network segment. `arp -a` displays the host's current cache of resolved IP-to-MAC bindings.
7. **What is the difference between dynamic and static ARP entries?**
   - *Answer*: Dynamic entries are learned automatically via broadcast requests and expire after a timeout; static entries are manually entered by an administrator and persist until reboot.
8. **What does `nslookup` do and what is a non-authoritative answer?**
   - *Answer*: `nslookup` queries DNS servers for domain name records. A non-authoritative answer means the response came from a recursive DNS cache rather than the authoritative primary server for that domain.
9. **What does `getmac` do?**
   - *Answer*: `getmac` retrieves the Media Access Control (MAC) 48-bit hardware physical addresses and transport names for all network adapters on the machine.
10. **How does `pathping` differ from standard `tracert`?**
    - *Answer*: `pathping` combines features of `tracert` and `ping` by first mapping the hop path and then sending multiple pings to each hop over time to calculate hop-by-hop packet loss percentages.
11. **What does a TTL value in a ping reply indicate?**
    - *Answer*: TTL (Time-To-Live) indicates how many hops the packet can survive before being discarded; the received value helps identify the remote operating system's default initial TTL (Linux=64, Windows=128, Cisco=255).
12. **Why does `nslookup` return multiple IP addresses for websites like google.com?**
    - *Answer*: Large services utilize DNS round-robin and content delivery networks (CDNs) to distribute incoming user traffic across multiple server IPs for load balancing and fault tolerance.
13. **What is the loopback address and what is its purpose?**
    - *Answer*: `127.0.0.1` (IPv4) or `::1` (IPv6) is the loopback address used to test the local TCP/IP protocol stack software without transmitting physical network packets.
14. **What is the difference between TCP `LISTENING` and `ESTABLISHED` states in `netstat`?**
    - *Answer*: `LISTENING` means a server socket is waiting for incoming client connection requests; `ESTABLISHED` means an active two-way connection has completed the TCP 3-way handshake.

---

## 13. Practical Viva Follow-Up Questions
- **Q: Why do some hops in `tracert` show `* * * Request timed out`?**
  - *Answer*: Intermediate routers or firewalls are configured to drop or deprioritize ICMP Time Exceeded packets for security or bandwidth preservation.
- **Q: Why is `netstat -n` preferred when diagnosing a heavily loaded server?**
  - *Answer*: Without `-n`, `netstat` tries to perform reverse DNS lookups for every remote IP, causing severe delays if DNS servers are slow or unreachable.

---

## 14. Lab-Record Screenshot Checklist
1. [ ] Screenshot of `ping 127.0.0.1` and `ping 8.8.8.8` showing 0% packet loss and measured RTT.
2. [ ] Screenshot of `ipconfig /all` displaying Ethernet/Wi-Fi adapter details (IPv4, Subnet Mask, Gateway, MAC).
3. [ ] Screenshot of `netstat -an` showing `LISTENING` and `ESTABLISHED` ports.
4. [ ] Screenshot of `nslookup google.com` showing resolved IP addresses.
5. [ ] Screenshot of `tracert 8.8.8.8` listing router hops.
6. [ ] Screenshot of `arp -a` displaying dynamic IP-to-MAC resolution table.
7. [ ] Screenshot of `getmac /v` showing physical network cards.

---

## 15. Result Statement
Thus, the program for demonstrating various network commands was executed and verified successfully on the Windows operating system.

---

## 16. Manual Fidelity Section
- **Preserved from Manual**: Exact coverage of all required diagnostic tools (`ping`, `netstat`, `ipconfig`, `nslookup`, `traceroute`, `arp`, `hostname`, `systeminfo`, `getmac`, and `pathping`).
- **Implementation Enhancements**: Windows-native command mapping (`tracert` for Unix traceroute), structured batch runner (`run_commands.bat`) and PowerShell runner (`run_commands.ps1`).
