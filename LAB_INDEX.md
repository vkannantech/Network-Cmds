# 24IT5611 Networks Laboratory: Master Experiment Index

**Academic Year**: 2026–2027 (Odd)  
**Course Code**: 24IT5611 – Networks Laboratory  
**Department**: Department of Information Technology, Francis Xavier Engineering College  
**Reference Manual**: `Network Lab Manual.pdf`

---

## Complete Experiments Mapping Table (Exercises 1–10)

| Ex. No. | Manual Experiment Title | Primary Protocol / Algorithm | Technology / Tool | Port / Sample Parameters | Directory Link |
| :---: | :--- | :--- | :--- | :--- | :--- |
| **1** | Demonstrate various network commands. | ICMP, ARP, DNS, TCP/IP Stack Diagnostics | Windows CLI / PowerShell | `ping`, `netstat`, `ipconfig`, `nslookup`, `tracert`, `arp`, `hostname`, `systeminfo`, `getmac`, `pathping` | [exc-01-network-commands](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-01-network-commands/README.md) |
| **2** | Implementation of HTTP and DNS protocols. | HTTP (TCP Image Transfer) & DNS (UDP Address Resolution) | Java Standard Edition (`Socket`, `DatagramSocket`, Swing) | HTTP Port: `387` (`network.jpg`)<br>DNS Port: `1309` (`www.aptitudeguru.com` <-> `165.165.80.80`) | [exc-02-http-dns](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/README.md) |
| **3** | Implementation of sliding window and CRC protocols. | Flow Control (Sliding Window) & Error Detection (CRC Modulo-2) | Java Standard Edition | Sliding Window: Frames=`8`, Window=`4`<br>CRC: Data=`1001101`, Gen=`1011` | [exc-03-sliding-window-crc](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-03-sliding-window-crc/README.md) |
| **4** | Implementation of Distance Vector routing protocols. | Bellman-Ford Routing Algorithm | Java Standard Edition | 4-Node Cost Matrix: `0 2 999 1`, `2 0 3 999`, `999 3 0 4`, `1 999 4 0` | [exc-04-distance-vector](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-04-distance-vector/README.md) |
| **5** | Configure a network using Link state routing protocols. | Dijkstra's Single-Source Shortest Path (SPF) | Java Standard Edition | 5-Node Matrix, Source=`0`<br>Distances: `0, 10, 50, 30, 60` | [exc-05-link-state](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-05-link-state/README.md) |
| **6** | Implement Address Resolution Protocol (ARP) and Reverse Address Resolution Protocol (RARP). | IP-to-MAC (ARP) & MAC-to-IP (RARP) Resolution | Java Standard Edition | Table: `192.168.1.1` to `.3`<br>MAC: `AA:BB:CC:11:22:33` to `..99` | [exc-06-arp-rarp](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-06-arp-rarp/README.md) |
| **7** | Develop a client-server based TCP application using socket programming functions. | TCP Connection-Oriented Stream Communication | Java Standard Edition (`ServerSocket`, `DataInputStream`) | TCP Port: `5000`<br>Msg: `"Hello Server"` -> `"Message received successfully"` | [exc-07-tcp-socket](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-07-tcp-socket/README.md) |
| **8** | Develop a client-server based UDP application using socket programming functions. | UDP Connectionless Datagram Communication | Java Standard Edition (`DatagramSocket`, `DatagramPacket`) | UDP Port: `5000`<br>Msg: `"Hello Server"` -> `"Message received successfully"` | [exc-08-udp-socket](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-08-udp-socket/README.md) |
| **9** | Study OPNET; performance analysis of TCP/UDP protocol using OPNET. | Discrete Event Simulation, TCP Congestion Control & RIP Dynamics | OPNET Modeler / IT Guru Academic Edition | Scenarios: `No_Drop`, `Drop_NoFast`, `Drop_Fast`<br>Routing: `NO_Failure`, `Failure` (Link cut at 200s) | [exc-09-opnet](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-09-opnet/README.md) |
| **10** | Simulation using Packet Tracer - Design a Network using Packet Tracer. | SOHO Hybrid Network Design, DHCP, DNS, Wireless & Broadband WAN | Cisco Packet Tracer 8.x / 7.x | SSID: `HomeNetwork`<br>Server: `208.67.220.220` (`Cisco.com`)<br>DHCP Pool: `DHCPpool` | [exc-10-packet-tracer](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-10-packet-tracer/README.md) |
