# 🌐 24IT5611 – Networks Laboratory (Exercises 1–10)
### Complete Java Implementations, CLI Diagnostic Tools, OPNET & Cisco Packet Tracer Simulations, Batch/PowerShell Runners, and 140+ Viva Voce Question Bank

<div align="center">

[![Java Version](https://img.shields.io/badge/Java-SE%208%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010%20%7C%2011-0078D6?style=for-the-badge&logo=windows&logoColor=white)](https://www.microsoft.com/windows)
[![Build Status](https://img.shields.io/badge/Verification-100%25%20PASS-brightgreen?style=for-the-badge&logo=checkmarx&logoColor=white)](VERIFICATION_REPORT.md)
[![Packet Tracer](https://img.shields.io/badge/Cisco-Packet%20Tracer%208.x-1BA0D7?style=for-the-badge&logo=cisco&logoColor=white)](https://www.netacad.com/courses/packet-tracer)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)
[![Academic Year](https://img.shields.io/badge/Academic%20Year-2026--2027%20(Odd)-purple?style=for-the-badge)](#)

</div>

---

## 📌 Table of Contents
- [📖 Project Overview](#-project-overview)
- [🎯 Course & Syllabus Mapping](#-course--syllabus-mapping)
- [⚡ Quick Start & Interactive Runner](#-quick-start--interactive-runner)
- [📂 Laboratory Exercises Breakdown (1–10)](#-laboratory-exercises-breakdown-110)
  - [Ex 1: Network Commands](#ex-1-demonstrate-various-network-commands)
  - [Ex 2: HTTP & DNS Protocols](#ex-2-implementation-of-http-and-dns-protocols)
  - [Ex 3: Sliding Window & CRC](#ex-3-implementation-of-sliding-window-and-crc-protocols)
  - [Ex 4: Distance Vector Routing](#ex-4-implementation-of-distance-vector-routing-protocol)
  - [Ex 5: Link State Routing (Dijkstra)](#ex-5-configure-a-network-using-link-state-routing-protocol)
  - [Ex 6: ARP & RARP Resolution](#ex-6-implementation-of-arp-and-rarp-protocols)
  - [Ex 7: TCP Socket Programming](#ex-7-client-server-tcp-socket-application)
  - [Ex 8: UDP Socket Programming](#ex-8-client-server-udp-socket-application)
  - [Ex 9: OPNET Simulation & Analysis](#ex-9-opnet-simulation--tcpudp-performance-analysis)
  - [Ex 10: Cisco Packet Tracer Network Design](#ex-10-cisco-packet-tracer-soho-network-design)
- [🔍 Mathematical & Protocol Integrity Audits](#-mathematical--protocol-integrity-audits)
- [🎓 Viva Voce Master Question Bank](#-viva-voce-master-question-bank)
- [📑 Laboratory Records & Submission Checklist](#-laboratory-records--submission-checklist)
- [🛠️ Technologies & Tools](#️-technologies--tools)
- [📜 License & Academic Citation](#-license--academic-citation)

---

## 📖 Project Overview

This repository provides an enterprise-grade, university-curated laboratory suite for **24IT5611 – Networks Laboratory (Department of Information Technology, Francis Xavier Engineering College, Academic Year 2026–2027 Odd)**.

### ✨ Key Features
- 🚀 **100% Standard Java (Zero External Dependencies)**: Compatible with standard JDK 8+, clean stream/socket closures, and structured error handling.
- ⚡ **Dual Runtime Launchers (`.bat` & `.ps1`)**: Native support for Windows Command Prompt (`cmd.exe`) and PowerShell 7/Windows PowerShell with `%~dp0` path resolution.
- 🎛️ **Master Interactive Control Centers (`run_lab.bat` & `run_lab.ps1`)**: Single-menu access to run any lab, compile all programs, and trigger automated test suites.
- 📚 **Comprehensive Lab Documentation (`README.md` per lab)**: Aims, algorithms, source codes, sample I/O, mathematical traces, and common errors.
- 🧠 **140+ Viva Voce Question Bank**: 15–30 second concise spoken answers for college viva examinations and technical interviews.
- 🔬 **Verified Audit Trail (`VERIFICATION_REPORT.md`)**: Complete compilation logs and deterministic test run evidence.

---

## 🎯 Course & Syllabus Mapping

| Ex. No. | Official Manual Title | Protocol / Algorithm | Implementation Language / Tool | Status |
| :---: | :--- | :--- | :--- | :---: |
| **1** | Demonstrate various network commands | ICMP, ARP, DNS, TCP/IP Layer Diagnostics | Windows CLI / PowerShell | `PASS` |
| **2** | Implementation of HTTP and DNS protocols | HTTP (TCP Image Transfer) & DNS (UDP) | Java SE (`Socket`, `DatagramSocket`) | `PASS` |
| **3** | Implementation of sliding window and CRC protocols | Flow Control (Sliding Window) & Error Detection (CRC) | Java SE (GF(2) Polynomial XOR) | `PASS` |
| **4** | Implementation of Distance Vector routing protocols | Bellman-Ford Routing Algorithm | Java SE ($4 \times 4$ Cost Matrix) | `PASS` |
| **5** | Configure a network using Link state routing protocols | Dijkstra's Single-Source Shortest Path (SPF) | Java SE ($5 \times 5$ Cost Matrix) | `PASS` |
| **6** | Implement Address Resolution Protocol (ARP) & RARP | Layer-3 IP $\leftrightarrow$ Layer-2 MAC Resolution | Java SE (Interactive Simulation) | `PASS` |
| **7** | Client-Server TCP socket application | TCP Connection-Oriented Stream Sockets | Java SE (`ServerSocket` Port 5000) | `PASS` |
| **8** | Client-Server UDP socket application | UDP Connectionless Datagram Sockets | Java SE (`DatagramSocket` Port 5000) | `PASS` |
| **9** | Study of OPNET; TCP/UDP & RIP performance analysis | Discrete Event Simulation & RIP Link Failure | OPNET Modeler / IT Guru | `PASS (Config)` |
| **10** | Simulation using Packet Tracer - Design a Network | SOHO Hybrid Network, DHCP, DNS, Wireless WAN | Cisco Packet Tracer 8.x / 7.x | `PASS (Config)` |

---

## ⚡ Quick Start & Interactive Runner

### 1. Launch Master Interactive Control Center
Open your preferred terminal in the repository root:

- **Windows PowerShell**:
  ```powershell
  .\run_lab.ps1
  ```
- **Windows Command Prompt (CMD)**:
  ```cmd
  run_lab.bat
  ```

---

## 📂 Laboratory Exercises Breakdown (1–10)

### Ex 1: Demonstrate Various Network Commands
- **Focus**: Host reachability, routing paths, socket states, MAC addresses, DNS diagnosis.
- **Commands Covered**: `ping`, `netstat -an`, `ipconfig /all`, `nslookup`, `tracert`, `arp -a`, `hostname`, `systeminfo`, `getmac /v`, `pathping`.
- **Runners**: [`run_commands.bat`](exc-01-network-commands/run_commands.bat) | [`run_commands.ps1`](exc-01-network-commands/run_commands.ps1) | [`Documentation`](exc-01-network-commands/README.md)

### Ex 2: Implementation of HTTP and DNS Protocols
- **HTTP**: TCP port `387`, client transmits `network.jpg`, server renders image in Swing `JFrame`.
- **DNS**: UDP port `1309`, client queries hostname/IP, server searches lookup database table (`www.aptitudeguru.com` $\leftrightarrow$ `165.165.80.80`).
- **Runners**: [`compile.bat`](exc-02-http-dns/compile.bat) | [`run_http_server.bat`](exc-02-http-dns/run_http_server.bat) | [`run_http_client.bat`](exc-02-http-dns/run_http_client.bat) | [`Documentation`](exc-02-http-dns/README.md)

### Ex 3: Implementation of Sliding Window and CRC Protocols
- **Sliding Window**: Frames = `8`, Window = `4`, cumulative acknowledgment pipelining.
- **CRC Modulo-2**: Data bits = `1 0 0 1 1 0 1`, Generator = `1 0 1 1`, calculated remainder = `101`, codeword = `1001101101`.
- **Runners**: [`compile.bat`](exc-03-sliding-window-crc/compile.bat) | [`run_sliding_window.bat`](exc-03-sliding-window-crc/run_sliding_window.bat) | [`run_crc.bat`](exc-03-sliding-window-crc/run_crc.bat) | [`Documentation`](exc-03-sliding-window-crc/README.md)

### Ex 4: Implementation of Distance Vector Routing Protocol
- **Algorithm**: Bellman-Ford all-pairs shortest path dynamic programming with routing table displays for Router 1 through 4.
- **Runners**: [`compile_run.bat`](exc-04-distance-vector/compile_run.bat) | [`compile_run.ps1`](exc-04-distance-vector/compile_run.ps1) | [`Documentation`](exc-04-distance-vector/README.md)

### Ex 5: Configure a Network Using Link State Routing Protocol
- **Algorithm**: Dijkstra's Single-Source Shortest Path (SPF) on a 5-node graph from source Node `0`.
- **Runners**: [`compile_run.bat`](exc-05-link-state/compile_run.bat) | [`compile_run.ps1`](exc-05-link-state/compile_run.ps1) | [`Documentation`](exc-05-link-state/README.md)

### Ex 6: Implementation of ARP and RARP Protocols
- **Functionality**: Interactive menu for resolving Layer-3 IP to Layer-2 MAC (ARP) and MAC to IP (RARP).
- **Runners**: [`compile_run.bat`](exc-06-arp-rarp/compile_run.bat) | [`compile_run.ps1`](exc-06-arp-rarp/compile_run.ps1) | [`Documentation`](exc-06-arp-rarp/README.md)

### Ex 7: Client-Server TCP Socket Application
- **Architecture**: Connection-oriented stream communication on TCP port `5000` exchanging `"Hello Server"` and confirmation.
- **Runners**: [`run_server.bat`](exc-07-tcp-socket/run_server.bat) | [`run_client.bat`](exc-07-tcp-socket/run_client.bat) | [`Documentation`](exc-07-tcp-socket/README.md)

### Ex 8: Client-Server UDP Socket Application
- **Architecture**: Connectionless datagram messaging on UDP port `5000` using `DatagramSocket` and `DatagramPacket`.
- **Runners**: [`run_server.bat`](exc-08-udp-socket/run_server.bat) | [`run_client.bat`](exc-08-udp-socket/run_client.bat) | [`Documentation`](exc-08-udp-socket/README.md)

### Ex 9: OPNET Simulation & TCP/UDP Performance Analysis
- **Scenarios**: `No_Drop`, `Drop_NoFast`, `Drop_Fast` (10-minute TCP analysis) and `NO_Failure`, `Failure` (RIP link failure at 200s).
- **Runners**: [`open_opnet.bat`](exc-09-opnet/open_opnet.bat) | [`project-checklist.md`](exc-09-opnet/project-checklist.md) | [`Documentation`](exc-09-opnet/README.md)

### Ex 10: Cisco Packet Tracer SOHO Network Design
- **Topology**: PC, Laptop (`WPC300N`), Wireless Router (`HomeNetwork`), Cable Modem, Cloud, Cisco.com Server (`208.67.220.220`).
- **Runners**: [`open_packet_tracer.bat`](exc-10-packet-tracer/open_packet_tracer.bat) | [`project-checklist.md`](exc-10-packet-tracer/project-checklist.md) | [`Documentation`](exc-10-packet-tracer/README.md)

---

## 🔍 Mathematical & Protocol Integrity Audits

### Exercise 3 CRC Modulo-2 Division Audit
```text
Data: 1001101 (7 bits) | Generator: 1011 (4 bits -> 3 zeros appended: 1001101000)

Step 1:  1 0 0 1 1 0 1 0 0 0
       ^ 1 0 1 1
       ---------------------
         0 0 1 0 1 0 1 0 0 0
Step 2:      1 0 1 0 1 0 0 0
           ^ 1 0 1 1
           -----------------
             0 0 0 1 1 0 0 0
Step 3:            1 1 0 0 0
                 ^ 1 0 1 1
                 -----------
                   0 1 1 1 0
Step 4:              1 1 1 0
                   ^ 1 0 1 1
                   ---------
                     0 1 0 1  <--- Remainder (CRC bits) = 1 0 1
```
- **Audited Remainder**: `101`
- **Codeword**: `1001101101`
- *Note*: The manual's printed output sample text lists `100`/`1001101100` (typographical erratum). The underlying Java code executes standard modulo-2 division producing `101`.

---

## 🎓 Viva Voce Master Question Bank

The repository includes a dedicated [docs/viva-master.md](docs/viva-master.md) containing **140+ curated viva questions and concise spoken answers**:
- **Protocol Foundations**: TCP 3-way handshake, UDP 8-byte header, DNS root/iterative resolution, ARP cache poisoning.
- **Routing Dynamics**: Bellman-Ford count-to-infinity, Split Horizon, Poison Reverse, OSPF LSDB & Dijkstra SPF tree.
- **Network Simulation**: Discrete event simulation, TCP congestion window (Slow Start vs AIMD), Cisco DHCP DORA workflow.

---

## 📑 Laboratory Records & Submission Checklist

Students preparing their physical lab manuals and record notebooks can follow [docs/submission-checklist.md](docs/submission-checklist.md) for screenshots, output formatting, algorithm write-ups, and faculty verification criteria.

---

## 🛠️ Technologies & Tools
- **Programming Language**: Java SE (JDK 8 / 11 / 17 / 21)
- **Shells & Scripting**: Windows Batch (`cmd.exe`), PowerShell (`pwsh`)
- **Simulators**: Cisco Packet Tracer 8.x, OPNET IT Guru Academic Edition / Modeler
- **Protocols Studied**: ICMP, ARP, RARP, IPv4, IPv6, TCP, UDP, HTTP, DNS, RIP, OSPF

---

## 📜 License & Academic Citation
Distributed under the **MIT License**. See [`LICENSE`](LICENSE) for details.

```bibtex
@manual{24IT5611_networks_lab,
  title        = {24IT5611 – Networks Laboratory Workspace (Exercises 1–10)},
  author       = {vkannantech},
  year         = {2026},
  organization = {Department of Information Technology, Francis Xavier Engineering College},
  note         = {Academic Year 2026–2027 (Odd)}
}
```
