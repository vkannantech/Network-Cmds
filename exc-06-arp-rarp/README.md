# Exercise 6: Implementation of ARP and RARP Protocols

## 1. Experiment Number & Title
- **Experiment No**: 6
- **Manual Title**: Implement Address Resolution Protocol (ARP) and Reverse Address Resolution Protocol (RARP).
- **Course**: 24IT5611 – Networks Laboratory (Academic Year 2026–2027 Odd)

---

## 2. Aim
To write a Java program for simulating **ARP** (IP -> MAC address resolution) and **RARP** (MAC -> IP address resolution) using simulated mapping tables.

---

## 3. How to Run
- CMD: `compile_run.bat`
- PowerShell: `.\compile_run.ps1`

---

## 4. Sample Sessions
- Choice 1 (ARP): IP `192.168.1.2` -> MAC `AA:BB:CC:44:55:66`
- Choice 2 (RARP): MAC `AA:BB:CC:77:88:99` -> IP `192.168.1.3`
- Choice 3: Program terminated

---

## 5. Viva Questions & Answers (14 Questions)
1. **What is ARP?** Address Resolution Protocol (RFC 826); maps 32-bit IPv4 to 48-bit physical MAC address.
2. **What is RARP?** Reverse Address Resolution Protocol (RFC 903); maps 48-bit MAC address to IPv4 address.
3. **Why is ARP needed?** Layer-2 NICs only transmit frames using MAC addresses on the local link.
4. **Is ARP Request broadcast or unicast?** Broadcast (`FF:FF:FF:FF:FF:FF`).
5. **Is ARP Reply broadcast or unicast?** Unicast to requesting host's MAC.
6. **What is Gratuitous ARP?** Host queries its own IP to detect duplicates and update neighbor caches.
7. **What is ARP Spoofing?** Malicious host sends forged ARP replies to intercept traffic (MITM).
8. **What is Proxy ARP?** Router answers ARP requests on behalf of remote subnet hosts.
9. **Why is RARP obsolete?** Replaced by BOOTP and DHCP which provide gateway, subnet mask, and DNS.
10. **What is ARP Cache timer?** 2–20 minutes timeout to purge stale hardware bindings.
11. **What is ARP EtherType?** `0x0806` (RARP is `0x8035`, IPv4 is `0x0800`).
12. **Can ARP cross routers?** No; routers do not forward Layer-2 broadcasts.
13. **How does IPv6 do address resolution?** Uses ICMPv6 Neighbor Discovery Protocol (NDP).
14. **MAC address structure?** 24-bit OUI (manufacturer) + 24-bit NIC serial identifier.

---

## 6. Result Statement
Thus, the Java program for implementing ARP and RARP protocols was executed and verified successfully.
