# EXERCISE 1: DEMONSTRATE VARIOUS NETWORK COMMANDS

---

## 🎯 Aim

To study and demonstrate various essential networking commands in the operating system to understand host connectivity, network configuration, routing paths, active connections, and address resolution.

---

## 💻 Environment

- **Operating System:** Windows 11 Pro (x64)
- **Host Machine:** KANNAN-V
- **Network Interface:** Wi-Fi (`Realtek RTL8852`)
- **Tools / Utilities:** Command Prompt (`cmd.exe`) / PowerShell

---

## 📋 Summary of Commands Covered

| S.No | Command                     | Equivalent in Linux     | Purpose                                                                |
| :--- | :-------------------------- | :---------------------- | :--------------------------------------------------------------------- |
| 1    | `ping`                      | `ping`                  | Tests end-to-end connectivity and packet round-trip time.              |
| 2    | `ipconfig`                  | `ifconfig` / `ip addr`  | Displays IP addresses, subnet masks, and default gateways.             |
| 3    | `netstat`                   | `netstat` / `ss`        | Shows active network connections, ports, and listening sockets.        |
| 4    | `nslookup`                  | `nslookup` / `dig`      | Queries DNS servers for domain name resolution and IP mapping.         |
| 5    | `tracert`                   | `traceroute`            | Traces each intermediate router hop to reach a destination host.       |
| 6    | `arp -a`                    | `arp -a` / `ip neigh`   | Displays the ARP table mapping IP addresses to physical MAC addresses. |
| 7    | `hostname`                  | `hostname`              | Displays the computer name of the current machine.                     |
| 8    | `systeminfo`                | `uname -a` / `lshw`     | Displays detailed OS configuration and hardware specifications.        |
| 9    | `getmac`                    | `ip link`               | Displays Media Access Control (MAC) addresses of installed adapters.   |
| 10   | `pathping`                  | `mtr`                   | Combines `ping` and `traceroute` to detect packet loss per hop.        |
| 11   | _(Optional)_ Packet Capture | `tcpdump` / `Wireshark` | Captures and analyzes live network packets passing through the NIC.    |

---

## 🧪 Detailed Execution & Live Outputs

### 1️⃣ `ping` (Packet Internet Groper)

- **Purpose:** Uses ICMP Echo Request and Echo Reply messages to verify that a target host is reachable across an IP network and measures round-trip latency.
- **Syntax:** `ping <hostname or IP>`

```cmd
C:\> ping google.com
```

#### Actual Output:

```text
Pinging google.com [2404:6800:4007:816::200e] with 32 bytes of data:
Reply from 2404:6800:4007:816::200e: time=65ms
Reply from 2404:6800:4007:816::200e: time=77ms
Reply from 2404:6800:4007:816::200e: time=133ms
Reply from 2404:6800:4007:816::200e: time=44ms

Ping statistics for 2404:6800:4007:816::200e:
    Packets: Sent = 4, Received = 4, Lost = 0 (0% loss),
Approximate round trip times in milli-seconds:
    Minimum = 44ms, Maximum = 133ms, Average = 79ms
```

---

### 2️⃣ `ipconfig` (IP Configuration)

- **Purpose:** Displays all current TCP/IP network configuration values (IPv4, IPv6, Subnet Mask, Default Gateway). Use `ipconfig /all` for detailed adapter info including DHCP and DNS servers.
- **Syntax:** `ipconfig` or `ipconfig /all`

```cmd
C:\> ipconfig
```

#### Actual Output:

```text
Windows IP Configuration

Wireless LAN adapter Wi-Fi:

   IPv6 Address. . . . . . . . . . . : 2409:40f4:100f:73:2664:21a9:861e:d55d
   Temporary IPv6 Address. . . . . . : 2409:40f4:100f:73:29e6:ccf3:ae6b:667b
   Link-local IPv6 Address . . . . . : fe80::a913:23d0:1c02:3953%23
   IPv4 Address. . . . . . . . . . . : 10.76.100.84
   Subnet Mask . . . . . . . . . . . : 255.255.255.0
   Default Gateway . . . . . . . . . : fe80::a870:1cff:fe36:7849%23
                                       10.76.100.153
```

---

### 3️⃣ `netstat` (Network Statistics)

- **Purpose:** Displays active TCP connections, ports on which the computer is listening, Ethernet statistics, the IP routing table, and protocol statistics.
- **Syntax:** `netstat -an` (`-a`: all connections and listening ports, `-n`: numerical addresses)

```cmd
C:\> netstat -an
```

#### Actual Output:

```text
Active Connections

  Proto  Local Address          Foreign Address        State
  TCP    0.0.0.0:135            0.0.0.0:0              LISTENING
  TCP    0.0.0.0:445            0.0.0.0:0              LISTENING
  TCP    10.76.100.84:139       0.0.0.0:0              LISTENING
  TCP    10.76.100.84:49927     18.178.183.62:443      ESTABLISHED
  TCP    10.76.100.84:50596     18.178.183.62:443      ESTABLISHED
  TCP    10.76.100.84:51725     54.243.226.44:443      ESTABLISHED
  TCP    10.76.100.84:53005     34.149.211.227:443     ESTABLISHED
```

---

### 4️⃣ `nslookup` (Name Server Lookup)

- **Purpose:** Queries the Domain Name System (DNS) to obtain the mapping between domain name and IP address, or other DNS records.
- **Syntax:** `nslookup <domain-name>`

```cmd
C:\> nslookup google.com
```

#### Actual Output:

```text
Server:  UnKnown
Address:  10.76.100.153

Non-authoritative answer:
Name:    google.com
Addresses:  2404:6800:4007:81b::200e
          142.251.43.142
```

---

### 5️⃣ `tracert` (Trace Route)

- **Purpose:** Determines the path that an IP packet takes to reach a destination by incrementing ICMP Time-To-Live (TTL) values.
- **Syntax:** `tracert <hostname or IP>`

```cmd
C:\> tracert -h 10 google.com
```

#### Actual Output:

```text
Tracing route to google.com [2404:6800:4007:816::200e]
over a maximum of 10 hops:

  1     4 ms     4 ms     4 ms  2409:40f4:100f:73::6a
  2     *        *        *     Request timed out.
  3    53 ms    45 ms    35 ms  2405:200:5218:20:3925::ff06
  4     *        *        *     Request timed out.
  5     *        *        *     Request timed out.
  6    32 ms    34 ms   255 ms  2405:200:801:2300::658
  7     *        *        *     Request timed out.
  8   164 ms   101 ms   102 ms  2001:4860:1:1::170
  9   145 ms   101 ms   102 ms  2001:4860:1:1::170
 10    94 ms    49 ms    58 ms  2404:6800:8201:1c0::1

Trace complete.
```

---

### 6️⃣ `arp -a` (Address Resolution Protocol)

- **Purpose:** Displays the local Address Resolution Protocol (ARP) cache table showing resolved IP (Logical) addresses to MAC (Physical) addresses.
- **Syntax:** `arp -a`

```cmd
C:\> arp -a
```

#### Actual Output:

```text
Interface: 10.76.100.84 --- 0x17
  Internet Address      Physical Address      Type
  10.76.100.153         aa-70-1c-36-78-49     dynamic
  224.0.0.22            01-00-5e-00-00-16     static
  224.0.0.251           01-00-5e-00-00-fb     static
  239.255.255.250       01-00-5e-7f-ff-fa     static
  255.255.255.255       ff-ff-ff-ff-ff-ff     static
```

---

### 7️⃣ `hostname`

- **Purpose:** Displays the host name (network identifier name) of the current computer.
- **Syntax:** `hostname`

```cmd
C:\> hostname
```

#### Actual Output:

```text
KANNAN-V
```

---

### 8️⃣ `systeminfo`

- **Purpose:** Displays detailed configuration information about a computer and its operating system, including network cards, RAM, and OS version.
- **Syntax:** `systeminfo`

```cmd
C:\> systeminfo
```

#### Actual Output:

```text
Host Name:                     KANNAN-V
OS Name:                       Microsoft Windows 11 Pro Insider Preview
OS Version:                    10.0.29648 N/A Build 29648
OS Manufacturer:               Microsoft Corporation
OS Configuration:              Standalone Workstation
System Manufacturer:           Dell Inc.
System Model:                  Inspiron 15 3530
System Type:                   x64-based PC
Total Physical Memory:         16,069 MB
```

---

### 9️⃣ `getmac` (Get MAC Address)

- **Purpose:** Returns the Media Access Control (MAC) address and list of network protocols associated with each network adapter.
- **Syntax:** `getmac /v`

```cmd
C:\> getmac /v
```

#### Actual Output:

```text
Connection Name Network Adapter Physical Address    Transport Name
=============== =============== =================== ==========================================================
Wi-Fi           Realtek RTL8852 C8-A3-E8-16-56-95   \Device\Tcpip_{FD30E754-E1DE-4005-8FE1-F2A6E00E48CB}
Bluetooth Netwo Bluetooth Devic C8-A3-E8-16-56-96   Media disconnected
```

---

### 🔟 `pathping`

- **Purpose:** Combines functionality of `ping` and `tracert` by sending packets to each router on the way to a final destination and calculating statistics per hop to pinpoint network loss.
- **Syntax:** `pathping <hostname or IP>`

```cmd
C:\> pathping -n -h 3 -p 250 -q 2 google.com
```

#### Actual Output:

```text
Tracing route to google.com [2404:6800:4007:816::200e]
over a maximum of 3 hops:
  0  2409:40f4:100f:73:29e6:ccf3:ae6b:667b
  1  2409:40f4:100f:73::6a
  2     *        *        *
Computing statistics for 0 seconds...
            Source to Here   This Node/Link
Hop  RTT    Lost/Sent = Pct  Lost/Sent = Pct  Address
  0                                           2409:40f4:100f:73:29e6:ccf3:ae6b:667b
                                0/   2 =  0%   |
  1   11ms     0/   2 =  0%     0/   2 =  0%  2409:40f4:100f:73::6a

Trace complete.
```

---

## 📌 Viva & Concept Review

1. **What is the difference between `ping` and `traceroute` (`tracert`)?**
   - `ping` tests if a remote host is reachable and calculates average latency.
   - `tracert` displays the exact sequence of intermediate routers (hops) traversed along the path to the destination.

2. **What protocol does `ping` use?**
   - ICMP (Internet Control Message Protocol), specifically ICMP Echo Request (Type 8) and ICMP Echo Reply (Type 0).

3. **What is the role of ARP?**
   - ARP (Address Resolution Protocol) resolves a Layer 3 logical IP address to a Layer 2 physical MAC address on a local area network.

4. **Why is `tcpdump` / `Wireshark` used?**
   - They are packet analyzer / sniffer tools used to capture and inspect live network traffic packet-by-packet for debugging and protocol verification.

---

## 🏁 Result

All basic networking commands were executed successfully on the system, and their output and behavior were studied and documented.
