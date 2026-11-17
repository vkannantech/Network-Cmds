# 🌐 Networks-Commands-Deeply: Master Engineering Module Index

| Module | Title | Primary Protocol / Algorithm | Technology / Tool | Default Coordinates / Test Inputs | Documentation |
| :---: | :--- | :--- | :--- | :--- | :--- |
| **01** | Diagnostic Network Commands | ICMP, ARP, DNS, TCP/IP Layer Diagnostics | Windows CLI / PowerShell | `ping`, `netstat`, `ipconfig`, `nslookup`, `tracert`, `arp`, `hostname`, `systeminfo`, `getmac`, `pathping` | [Module 01](exc-01-network-commands/README.md) |
| **02** | HTTP & DNS Protocol Implementations | HTTP (TCP Image Stream) & DNS (UDP Datagrams) | Java SE (`Socket`, `DatagramSocket`, Swing) | HTTP Port: `387` (`network.jpg`)<br>DNS Port: `1309` (`www.aptitudeguru.com` <-> `165.165.80.80`) | [Module 02](exc-02-http-dns/README.md) |
| **03** | Sliding Window & CRC Modulo-2 | Pipelined Flow Control & Polynomial Checksum | Java SE (GF(2) Binary XOR) | Window: Frames=`8`, Window=`4`<br>CRC: Data=`1001101`, Gen=`1011` -> `101` | [Module 03](exc-03-sliding-window-crc/README.md) |
| **04** | Distance Vector Routing Protocol | Bellman-Ford Shortest Path Algorithm | Java SE | 4-Node Cost Matrix (`0 2 999 1`, `2 0 3 999`, `999 3 0 4`, `1 999 4 0`) | [Module 04](exc-04-distance-vector/README.md) |
| **05** | Link State Routing Protocol | Dijkstra's Single-Source Shortest Path (SPF) | Java SE | 5-Node Cost Matrix, Source: Node `0`<br>Distances: `0, 10, 50, 30, 60` | [Module 05](exc-05-link-state/README.md) |
| **06** | ARP & RARP Address Resolution | Layer-3 IP <-> Layer-2 MAC Mapping Engine | Java SE | Table: `192.168.1.1`–`.3` <-> `AA:BB:CC:11:22:33`–`..99` | [Module 06](exc-06-arp-rarp/README.md) |
| **07** | Client-Server TCP Stream Socket | Connection-Oriented Stream Communication | Java SE (`ServerSocket`, `DataInputStream`) | TCP Port: `5000`<br>Payload: `"Hello Server"` -> `"Message received successfully"` | [Module 07](exc-07-tcp-socket/README.md) |
| **08** | Client-Server UDP Datagram Socket | Connectionless Datagram Communication | Java SE (`DatagramSocket`, `DatagramPacket`) | UDP Port: `5000`<br>Payload: `"Hello Server"` -> `"Message received successfully"` | [Module 08](exc-08-udp-socket/README.md) |
| **09** | OPNET Simulation & Analysis | Discrete Event Simulation & RIP Dynamics | OPNET IT Guru / Modeler | Scenarios: `No_Drop`, `Drop_NoFast`, `Drop_Fast`<br>Routing: `NO_Failure`, `Failure` (200s link cut) | [Module 09](exc-09-opnet/README.md) |
| **10** | Cisco Packet Tracer SOHO Network | SOHO Hybrid Network, DHCP, DNS, Wireless WAN | Cisco Packet Tracer 8.x / 7.x | SSID: `HomeNetwork`<br>Server: `208.67.220.220` (`Cisco.com`, `DHCPpool`) | [Module 10](exc-10-packet-tracer/README.md) |
