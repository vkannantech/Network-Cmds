# Networks-Commands-Deeply Suite: Master Viva Voce Question Bank (Exercises 1–10)

This master document contains **140+ high-yield viva voce questions and concise 15–30 second spoken answers** categorized by experiment for comprehensive oral examination preparation.

---

## Table of Contents

1. [Exercise 1: Network Commands](#exercise-1-network-commands)
2. [Exercise 2: HTTP and DNS Protocols](#exercise-2-http-and-dns-protocols)
3. [Exercise 3: Sliding Window & CRC Protocols](#exercise-3-sliding-window--crc-protocols)
4. [Exercise 4: Distance Vector Routing Protocol](#exercise-4-distance-vector-routing-protocol)
5. [Exercise 5: Link State Routing Protocol](#exercise-5-link-state-routing-protocol)
6. [Exercise 6: ARP and RARP Protocols](#exercise-6-arp-and-rarp-protocols)
7. [Exercise 7: TCP Socket Programming](#exercise-7-tcp-socket-programming)
8. [Exercise 8: UDP Socket Programming](#exercise-8-udp-socket-programming)
9. [Exercise 9: OPNET Simulation & Protocol Analysis](#exercise-9-opnet-simulation--protocol-analysis)
10. [Exercise 10: Cisco Packet Tracer Network Design](#exercise-10-cisco-packet-tracer-network-design)

---

## Exercise 1: Network Commands

1. **What is the primary function of the `ping` utility?**
   - _Answer_: `ping` tests network reachability between source and destination hosts and measures Round-Trip Time (RTT) using ICMP Echo Request (Type 8) and Echo Reply (Type 0) messages.
2. **Which transport layer protocol does `ping` use?**
   - _Answer_: `ping` does not use TCP or UDP; it operates directly above IP at Layer 3 using ICMP (Internet Control Message Protocol, IP protocol number 1).
3. **What is the difference between `ipconfig` and `ifconfig`?**
   - _Answer_: `ipconfig` is the native network configuration utility on Microsoft Windows systems, whereas `ifconfig` (and modern `ip addr`) is used on Unix/Linux systems.
4. **What information does `netstat -an` provide?**
   - _Answer_: It lists all active TCP/UDP connections and listening sockets with numerical IP addresses and port numbers without attempting slow DNS lookups.
5. **How does `tracert` determine intermediate router hops?**
   - _Answer_: It sends packets with incrementally increasing Time-To-Live (TTL) values starting from 1. Each router decrements TTL, drops the packet when TTL=0, and returns an ICMP Time Exceeded (Type 11) message.
6. **What is the purpose of the ARP protocol and what does `arp -a` show?**
   - _Answer_: ARP maps Layer-3 IPv4 addresses to Layer-2 physical MAC addresses. `arp -a` displays the host's current cache of resolved IP-to-MAC bindings.
7. **What is the difference between dynamic and static ARP entries?**
   - _Answer_: Dynamic entries are learned automatically via broadcast requests and expire after a timeout; static entries are manually entered by an administrator and persist until reboot.
8. **What does `nslookup` do and what is a non-authoritative answer?**
   - _Answer_: `nslookup` queries DNS servers for domain name records. A non-authoritative answer means the response came from a recursive DNS cache rather than the authoritative nameserver for that domain.
9. **What does `getmac` do?**
   - _Answer_: `getmac` retrieves the Media Access Control (MAC) 48-bit hardware physical addresses and network transport names for all network adapters on the host.
10. **How does `pathping` differ from standard `tracert`?**
    - _Answer_: `pathping` combines features of `tracert` and `ping` by first tracing the hop path and then sending multiple pings to each hop over time to calculate hop-by-hop packet loss percentages.
11. **What does a TTL value in a ping reply indicate?**
    - _Answer_: TTL (Time-To-Live) indicates how many hops the packet can survive before being discarded; the received value helps identify the remote operating system's default initial TTL (Linux=64, Windows=128, Cisco=255).
12. **Why does `nslookup` return multiple IP addresses for websites like google.com?**
    - _Answer_: Large web services utilize DNS round-robin and content delivery networks (CDNs) to distribute incoming user traffic across multiple server IPs for load balancing and fault tolerance.
13. **What is the loopback address and what is its purpose?**
    - _Answer_: `127.0.0.1` (IPv4) or `::1` (IPv6) is the loopback address used to test the local TCP/IP protocol stack software without transmitting physical network packets.
14. **What is the difference between TCP `LISTENING` and `ESTABLISHED` states in `netstat`?**
    - _Answer_: `LISTENING` means a server socket is waiting for incoming client connection requests; `ESTABLISHED` means an active two-way connection has completed the TCP 3-way handshake.

---

## Exercise 2: HTTP and DNS Protocols

15. **Why does HTTP use TCP rather than UDP for web transfers?**
    - _Answer_: HTTP requires reliable, error-free, and in-order data transmission. TCP provides positive acknowledgments, packet retransmission, and flow control necessary for web pages and binary assets.
16. **Why does DNS primarily use UDP instead of TCP?**
    - _Answer_: DNS queries are small, single-packet requests. UDP avoids the 3-way handshake overhead, minimizing latency and server state overhead.
17. **When does DNS fall back to TCP?**
    - _Answer_: DNS uses TCP port 53 when response payload exceeds 512 bytes (traditional UDP limit without EDNS0) or during DNS Zone Transfers (AXFR/IXFR) between authoritative name servers.
18. **What is the standard port number for HTTP and DNS in production networks?**
    - _Answer_: Standard HTTP uses TCP port 80 (HTTPS uses 443), while DNS uses UDP/TCP port 53. In this lab experiment, non-privileged educational ports 387 and 1309 are used.
19. **What is the role of `ByteArrayOutputStream` in the HTTP client?**
    - _Answer_: It provides an in-memory buffer to serialize a `BufferedImage` into a raw byte array so that the exact byte length can be computed and sent to the server.
20. **Why do we send the byte length of the image before sending the actual image data?**
    - _Answer_: TCP is a continuous byte stream without message boundaries (framing). Prepending the byte length allows the server to allocate the exact buffer and call `readFully()`.
21. **What is the difference between forward DNS and reverse DNS?**
    - _Answer_: Forward DNS maps a human-readable domain name to an IP address (A/AAAA record), while reverse DNS maps an IP address back to its host domain name (PTR record).
22. **What is a Datagram in UDP programming?**
    - _Answer_: A datagram is a self-contained, independent message containing source/destination IP addresses, port numbers, and payload, sent over the network without establishing an active connection.
23. **What does `readFully(byte[] b)` do in `DataInputStream`?**
    - _Answer_: It repeatedly reads bytes from the underlying input stream until the entire array `b` is filled, blocking until all requested bytes arrive or EOF is reached.
24. **What is the purpose of `InetAddress.getByName("127.0.0.1")`?**
    - _Answer_: It resolves the string IP address or hostname into a Java `InetAddress` object representing the Layer-3 network endpoint.
25. **Why is `flush()` called on the `ByteArrayOutputStream`?**
    - _Answer_: `flush()` forces any buffered output bytes to be written immediately to the underlying byte array before converting to `toByteArray()`.
26. **Can a single UDP socket receive messages from multiple different clients?**
    - _Answer_: Yes. Since UDP is connectionless, a single `DatagramSocket` bound to a port can receive datagrams from any remote client and inspect `receiver.getAddress()` and `receiver.getPort()`.
27. **What is iterative vs recursive DNS resolution?**
    - _Answer_: In recursive resolution, the local DNS server takes full responsibility to query root, TLD, and authoritative servers to return the final answer. In iterative resolution, the server returns referrals to next-level nameservers for the client to query.
28. **What is the default packet buffer size allocated for DNS datagrams in our implementation?**
    - _Answer_: 1024 bytes (`new byte[1024]`), which easily accommodates the query and response strings.

---

## Exercise 3: Sliding Window & CRC Protocols

29. **What is the primary advantage of Sliding Window over Stop-and-Wait?**
    - _Answer_: Sliding Window allows transmitting multiple frames before receiving an acknowledgment, keeping the network link continuously utilized and drastically improving throughput.
30. **What does the window size represent in flow control?**
    - _Answer_: It represents the maximum number of unacknowledged frames the sender is permitted to transmit into the network pipeline before waiting for an ACK.
31. **What is the difference between Go-Back-N and Selective Repeat?**
    - _Answer_: Go-Back-N retransmits all frames from the corrupted frame onward if an error occurs. Selective Repeat retransmits only the specific lost or damaged frame using individual receiver buffers.
32. **What is CRC and what layer of the OSI model does it operate on?**
    - _Answer_: CRC (Cyclic Redundancy Check) is an error-detecting code based on polynomial division operating at the Data Link Layer (Layer 2, Ethernet FCS) and Transport Layer.
33. **Why is XOR operation used in CRC calculation?**
    - _Answer_: Modulo-2 arithmetic operates without carry or borrow; in binary, modulo-2 addition and subtraction are mathematically identical to bitwise XOR.
34. **How many zero bits are appended to the data bits before CRC division?**
    - _Answer_: $(m - 1)$ zero bits are appended, where $m$ is the number of bits in the generator polynomial divisor (degree of polynomial $r$).
35. **How does the receiver verify data integrity using CRC?**
    - _Answer_: The receiver divides the received codeword $T(x)$ by the same generator polynomial $G(x)$. If the remainder is all zeros, no transmission error is detected; otherwise, an error occurred.
36. **What types of errors can CRC detect with 100% certainty?**
    - _Answer_: All single-bit errors, all double-bit errors (if $G(x)$ has suitable factors), any odd number of bit errors (if $G(x)$ contains $(x+1)$ as a factor), and all burst errors of length $\le m-1$.
37. **What is a standard CRC polynomial used in Ethernet?**
    - _Answer_: CRC-32 (IEEE 802.3 Ethernet standard): $x^{32} + x^{26} + x^{23} + x^{22} + x^{16} + x^{12} + x^{11} + x^{10} + x^8 + x^7 + x^5 + x^4 + x^2 + x + 1$.
38. **What is cumulative acknowledgment?**
    - _Answer_: A single ACK packet acknowledging receipt of all frames up to sequence number $N$, implying that all preceding frames were received correctly.
39. **What is piggybacking in sliding window protocols?**
    - _Answer_: Temporarily delaying outgoing acknowledgments so they can be hooked onto outgoing data frames traveling in the reverse direction to save bandwidth.
40. **Why must the most significant bit (MSB) of the generator polynomial always be 1?**
    - _Answer_: The MSB must be 1 so that division aligns properly with the leading non-zero bit of the dividend during modulo-2 subtraction.
41. **What is the maximum window size in Go-Back-N with $k$-bit sequence numbers?**
    - _Answer_: $2^k - 1$. For Selective Repeat, the maximum sender and receiver window size is $2^{k-1}$.
42. **What is bandwidth-delay product (BDP)?**
    - _Answer_: BDP $=$ Bandwidth $\times$ Round-Trip Time. It determines the optimal sliding window size needed to fully saturate the transmission pipeline.

---

## Exercise 4: Distance Vector Routing Protocol

43. **What algorithm does Distance Vector Routing use?**
    - _Answer_: Distance Vector Routing is based on the Bellman-Ford algorithm (or Ford-Fulkerson algorithm).
44. **How does Distance Vector differ from Link State Routing?**
    - _Answer_: In Distance Vector, routers exchange their entire routing tables only with immediate neighbors. In Link State, routers broadcast link-state advertisements (LSAs) about their direct neighbors to the entire network.
45. **What is the Bellman-Ford optimality equation?**
    - _Answer_: $D_x(y) = \min_v \{ c(x, v) + D_v(y) \}$, where $c(x,v)$ is cost to neighbor $v$ and $D_v(y)$ is $v$'s advertised distance to $y$.
46. **Which real-world routing protocol uses Distance Vector?**
    - _Answer_: RIP (Routing Information Protocol) and Cisco's IGRP/EIGRP (Advanced Distance Vector).
47. **What is the Count-to-Infinity problem?**
    - _Answer_: A routing loop issue in distance vector where routers continuously increment hop counts to an unreachable destination because of stale neighbor advertisements.
48. **What is Split Horizon?**
    - _Answer_: A rule stating that a router never advertises a route back out the same interface through which it originally learned that route.
49. **What is Poison Reverse?**
    - _Answer_: A technique where a router advertises an unreachable route back to the source neighbor with an infinite metric (metric 16 in RIP) to immediately break routing loops.
50. **What is the maximum hop count allowed in RIP?**
    - _Answer_: 15 hops. A hop count of 16 is treated as infinity (unreachable), bounding the convergence time.
51. **What does the value `999` represent in the input cost matrix?**
    - _Answer_: `999` represents an infinite cost, indicating there is no direct physical link between the two corresponding routers.
52. **Why is the distance from a router to itself always 0?**
    - _Answer_: Because traversing zero hops incurs zero transmission and queuing delay ($\text{cost}(i, i) = 0$).
53. **When does the Distance Vector algorithm achieve convergence?**
    - _Answer_: When no router receives any new vector update that changes its local shortest distance estimates.
54. **What is periodic vs triggered update in Distance Vector?**
    - _Answer_: Periodic updates are sent at fixed intervals (e.g. every 30 seconds in RIP), while triggered updates are sent immediately when a topology or link state change is detected.
55. **What is the time complexity of the all-pairs Bellman-Ford algorithm used here?**
    - _Answer_: $O(N^3)$, where $N$ is the number of routers/nodes in the network.
56. **Why is Distance Vector referred to as "routing by rumor"?**
    - _Answer_: Because routers do not have a complete map of the entire network topology; they calculate their shortest paths purely relying on the secondary information advertised by their neighbors.

---

## Exercise 5: Link State Routing Protocol

57. **What algorithm forms the foundation of Link State Routing?**
    - _Answer_: Dijkstra’s Shortest Path First (SPF) algorithm.
58. **Name two prominent real-world Link State routing protocols.**
    - _Answer_: OSPF (Open Shortest Path First, RFC 2328) and IS-IS (Intermediate System to Intermediate System).
59. **What is an LSA in Link State protocols?**
    - _Answer_: LSA (Link State Advertisement) is a packet generated by a router containing the state and cost of its attached physical links, flooded to all routers in the area.
60. **What is an LSDB (Link State Database)?**
    - _Answer_: An LSDB is a synchronized topological database containing all LSAs generated by all routers in the routing domain, giving each router a complete map of the entire network.
61. **How does Link State prevent routing loops?**
    - _Answer_: Because all routers possess a complete, identical map of the entire network and independently compute a Shortest Path Tree (SPT) with themselves as the root.
62. **What is the time complexity of Dijkstra's algorithm implemented with an adjacency matrix?**
    - _Answer_: $O(N^2)$, where $N$ is the number of vertices/routers. With a min-heap or priority queue and adjacency list, it reduces to $O((V + E) \log V)$.
63. **What does the relaxation condition `dist[u] + graph[u][v] < dist[v]` mean?**
    - _Answer_: It tests whether reaching vertex $v$ via intermediate node $u$ is shorter than the currently known shortest path to $v$.
64. **Why does Dijkstra's algorithm fail on graphs with negative edge weights?**
    - _Answer_: Dijkstra greedily assumes that once a node is marked visited, its shortest distance cannot decrease. Negative edge weights violate this non-decreasing path property (Bellman-Ford must be used instead).
65. **What is OSPF Area 0 (Backbone Area)?**
    - _Answer_: In hierarchical OSPF network designs, Area 0 is the central core backbone area to which all other non-backbone areas must connect to distribute inter-area routing information.
66. **What is the Designated Router (DR) and Backup Designated Router (BDR) in OSPF?**
    - _Answer_: On multi-access broadcast networks (like Ethernet), routers elect a DR and BDR to act as central points for exchanging LSAs, reducing LSA flooding traffic from $O(N^2)$ to $O(N)$.
67. **Why does Link State consume more memory and CPU than Distance Vector?**
    - _Answer_: Link State requires storing the entire network topology in memory (LSDB) and running computationally intensive SPF tree calculations whenever link states change.
68. **What metric does OSPF use to calculate link cost by default?**
    - _Answer_: Cost $= \frac{\text{Reference Bandwidth}}{\text{Interface Bandwidth}} = \frac{10^8 \text{ bps}}{\text{Bandwidth in bps}}$ (by default 100 Mbps / interface speed).
69. **What is the Dijkstra algorithm classification (Greedy vs Dynamic Programming)?**
    - _Answer_: Dijkstra is a greedy algorithm because at each step it selects the globally best local choice (the unvisited vertex with smallest tentative distance).
70. **In the sample graph, why is Node 4 distance 60 instead of direct 100?**
    - _Answer_: The direct link cost $0 \to 4$ is 100, but the multi-hop path $0 \to 3 \to 2 \to 4$ has a cost of $30 + 20 + 10 = 60$, which is significantly cheaper.

---

## Exercise 6: ARP and RARP Protocols

71. **What is the full form and primary purpose of ARP?**
    - _Answer_: Address Resolution Protocol (RFC 826). It maps a known 32-bit Layer-3 IPv4 address to a 48-bit physical Layer-2 MAC address on a local area network.
72. **What is the full form and primary purpose of RARP?**
    - _Answer_: Reverse Address Resolution Protocol (RFC 903). It maps a known 48-bit physical MAC address to an assigned 32-bit IPv4 address.
73. **Why is ARP needed if we already have IP addresses?**
    - _Answer_: IP addresses are logical and routable across internetworks, but physical Network Interface Cards (NICs) only transmit and receive frames based on 48-bit MAC addresses on the local Ethernet link.
74. **Is an ARP Request sent as a unicast or broadcast?**
    - _Answer_: An ARP Request is sent as a Layer-2 broadcast (`FF:FF:FF:FF:FF:FF`) so all hosts on the local network segment receive and inspect it.
75. **Is an ARP Reply sent as a unicast or broadcast?**
    - _Answer_: An ARP Reply is sent as a unicast frame directed specifically to the MAC address of the requesting host.
76. **What is Gratuitous ARP?**
    - _Answer_: An ARP broadcast where a host queries its own IP address to detect duplicate IP conflicts or to update neighbor ARP caches after a hardware or IP change.
77. **What is ARP Spoofing / Poisoning?**
    - _Answer_: A security attack where a malicious host sends forged ARP replies associating its own MAC address with the IP address of a legitimate gateway/server to intercept or alter network traffic (Man-In-The-Middle).
78. **What is Proxy ARP?**
    - _Answer_: A technique where a router answers ARP requests on behalf of remote hosts located on another subnet to enable transparent communication without default gateway configuration on the client.
79. **Why has RARP been made obsolete in modern networks?**
    - _Answer_: RARP operated at Layer 2, required a dedicated server on every physical subnet, and could only provide an IP address without subnet mask, default gateway, or DNS server information (superseded by BOOTP and DHCP).
80. **What is an ARP Cache and why does it have an expiration timer?**
    - _Answer_: An in-memory cache of recent IP-to-MAC mappings. Timers (typically 2–20 minutes) ensure stale or outdated entries are pruned when devices change NICs or disconnect.
81. **What Ethernet frame Type (EtherType) is assigned to ARP?**
    - _Answer_: `0x0806` for ARP (and `0x8035` for RARP). Standard IPv4 is `0x0800`.
82. **Can ARP cross a router to resolve an IP on a remote network?**
    - _Answer_: No. ARP broadcasts are bounded by the local Layer-2 broadcast domain (router interfaces do not forward broadcast frames). To reach a remote IP, the sender ARPs for its local default gateway's MAC address.
83. **How does IPv6 handle address resolution without ARP?**
    - _Answer_: IPv6 eliminates ARP entirely and uses ICMPv6 Neighbor Discovery Protocol (NDP) with Neighbor Solicitation (NS) and Neighbor Advertisement (NA) multicast messages.
84. **What is the structure of a standard 48-bit MAC address?**
    - _Answer_: The first 24 bits represent the Organizationally Unique Identifier (OUI) assigned to the manufacturer by IEEE; the last 24 bits represent the Network Interface Controller (NIC) specific serial identifier.

---

## Exercise 7: TCP Socket Programming

85. **What is a Socket in computer networking?**
    - _Answer_: A socket is an endpoint for bidirectional communication between two processes across a network, uniquely identified by an IP address, transport protocol (TCP/UDP), and port number.
86. **Explain the TCP Three-Way Handshake.**
    - _Answer_:
      1. Client sends `SYN` (synchronize sequence number).
      2. Server responds with `SYN-ACK` (acknowledges client sequence and sends its own).
      3. Client sends `ACK` (acknowledges server sequence). The connection is now established.
87. **What is the difference between `ServerSocket` and `Socket` classes in Java?**
    - _Answer_: `ServerSocket` is used by servers to listen for and accept inbound client connections, whereas `Socket` is used by both client and server to exchange data streams across an established connection.
88. **Why is TCP called a connection-oriented and reliable protocol?**
    - _Answer_: It requires establishing an active connection before transmitting data and guarantees ordered, error-free, and duplicate-free delivery using sequence numbers, ACKs, and retransmissions.
89. **What does `accept()` method return and what is its blocking behavior?**
    - _Answer_: `accept()` blocks the current thread until a client connects, returning a new `Socket` object dedicated specifically to communication with that individual client.
90. **What is the role of `DataInputStream.readUTF()` and `writeUTF()`?**
    - _Answer_: They serialize and deserialize Unicode strings in a format prefixed with a 2-byte unsigned length header, providing automatic message framing over continuous TCP streams.
91. **How does TCP achieve Flow Control?**
    - _Answer_: TCP uses a Sliding Window mechanism where the receiver advertises a Receive Window (`rwnd`) in packet headers, preventing a fast sender from overwhelming a slow receiver's buffer.
92. **How does TCP achieve Congestion Control?**
    - _Answer_: TCP dynamically adjusts its Congestion Window (`cwnd`) using Slow Start, Congestion Avoidance, Fast Retransmit, and Fast Recovery algorithms.
93. **What happens during TCP connection termination (Four-Way Handshake)?**
    - _Answer_:
      1. Active closer sends `FIN`.
      2. Receiver sends `ACK` and enters `CLOSE_WAIT`.
      3. Receiver sends its own `FIN`.
      4. Active closer sends `ACK` and enters `TIME_WAIT` before fully closing.
94. **Why does a closed TCP connection enter the `TIME_WAIT` state?**
    - _Answer_: To ensure the final `ACK` is received by the remote peer (and to retransmit if lost) and to allow any lingering duplicate packets from the old connection to expire in the network.
95. **Can multiple clients connect to the same server port concurrently?**
    - _Answer_: Yes. The OS differentiates incoming TCP connections using the unique 4-tuple: `(Source IP, Source Port, Destination IP, Destination Port)`.
96. **What is the range of well-known, registered, and dynamic port numbers?**
    - _Answer_: Well-known ports: `0–1023`; Registered ports: `1024–49151`; Dynamic/Private ports: `49152–65535`.
97. **What is the difference between a Stream socket and a Datagram socket?**
    - _Answer_: A stream socket (TCP, `SOCK_STREAM`) provides reliable, ordered, continuous byte stream delivery, while a datagram socket (UDP, `SOCK_DGRAM`) provides unreliable, discrete packet delivery.
98. **What is the function of `flush()` on an `OutputStream`?**
    - _Answer_: It forces any buffered bytes in memory to be pushed immediately across the network socket to the remote destination.

---

## Exercise 8: UDP Socket Programming

99. **What is UDP and why is it called connectionless and unreliable?**
    - _Answer_: User Datagram Protocol (RFC 768) transmits independent datagrams without establishing a handshake connection beforehand and provides no guarantee of packet delivery, ordering, or deduplication.
100. **What is the size of the UDP header compared to TCP?**
     - _Answer_: UDP header is fixed at 8 bytes (64 bits), whereas a standard TCP header is at least 20 bytes (up to 60 bytes with options).
101. **What fields are present in a standard UDP header?**
     - _Answer_:
       1. Source Port (16 bits)
       2. Destination Port (16 bits)
       3. Length (16 bits)
       4. Checksum (16 bits)
102. **What are the key differences between TCP and UDP?**
     - _Answer_:
       - **TCP**: Connection-oriented, reliable, byte-stream, heavier header (20B), flow & congestion control.
       - **UDP**: Connectionless, best-effort delivery, message-oriented (preserves packet boundaries), lightweight header (8B), no congestion control.
103. **Why do real-time streaming and VoIP prefer UDP over TCP?**
     - _Answer_: Real-time audio/video prioritize low latency over 100% reliability. Retransmitting dropped audio packets in TCP introduces jitter and lag that degrade live communication.
104. **What Java classes are used for UDP socket programming?**
     - _Answer_: `java.net.DatagramSocket` (to send and receive packets) and `java.net.DatagramPacket` (to encapsulate the data payload and destination address).
105. **How does the server know where to send the reply in UDP?**
     - _Answer_: The received `DatagramPacket` contains the sender's IP address (`dpReceive.getAddress()`) and ephemeral port number (`dpReceive.getPort()`).
106. **What does message-oriented framing mean in UDP?**
     - _Answer_: In UDP, one `send()` call produces exactly one discrete packet, and one `receive()` call reads exactly one complete packet, preserving message boundaries without manual stream delimiters.
107. **What happens if a UDP packet is larger than the Maximum Transmission Unit (MTU)?**
     - _Answer_: The IP layer fragments the packet into multiple IP fragments. If any single fragment is lost in transit, the entire UDP packet is dropped.
108. **Is the UDP checksum mandatory in IPv4 and IPv6?**
     - _Answer_: In IPv4, UDP checksum is optional (though usually enabled); in IPv6, the UDP checksum is mandatory.
109. **Can UDP perform broadcasting and multicasting?**
     - _Answer_: Yes. UDP natively supports one-to-many (multicast) and one-to-all (broadcast) communications, whereas standard TCP only supports point-to-point unicast.
110. **What is an ephemeral port?**
     - _Answer_: A short-lived, dynamically allocated port assigned automatically by the operating system kernel to a client application when sending outbound traffic.
111. **What is the purpose of `setSoTimeout()` on a `DatagramSocket`?**
     - _Answer_: It sets a blocking timeout (in milliseconds) for `receive()`. If no packet arrives within the timeout period, a `SocketTimeoutException` is thrown to prevent indefinite hanging.
112. **Why is `dpReceive.getLength()` used instead of `receive.length` when converting to String?**
     - _Answer_: `receive.length` is the total allocated buffer size (1024), while `dpReceive.getLength()` is the actual number of payload bytes received in the current datagram packet.

---

## Exercise 9: OPNET Simulation & Protocol Analysis

113. **What is OPNET Modeler / IT Guru?**
     - _Answer_: OPNET is a discrete-event network simulator used for modeling network protocols, evaluating traffic throughput, analyzing application performance, and designing enterprise network architectures.
114. **What is Discrete Event Simulation (DES)?**
     - _Answer_: A simulation modeling paradigm where system state changes occur instantaneously at discrete points in simulated time, skipping inactive periods between events.
115. **What is the difference between Slow Start and Congestion Avoidance in TCP?**
     - _Answer_: In Slow Start, $cwnd$ doubles every RTT (exponential growth) until it reaches `ssthresh`. In Congestion Avoidance, $cwnd$ increases by 1 MSS per RTT (linear growth) to probe capacity safely.
116. **What is TCP Fast Retransmit?**
     - _Answer_: A mechanism where the sender retransmits a missing segment immediately upon receiving 3 duplicate ACKs without waiting for the retransmission timer (RTO) to expire.
117. **What is TCP Fast Recovery?**
     - _Answer_: After Fast Retransmit, TCP sets `ssthresh = cwnd / 2` and sets `cwnd = ssthresh`, continuing linear Congestion Avoidance without dropping back to $cwnd = 1$ MSS.
118. **What is the default RIP update timer?**
     - _Answer_: 30 seconds. Every 30 seconds, routers broadcast their entire routing table to all active RIP neighbors.
119. **What is the RIP Route Invalidation / Timeout period?**
     - _Answer_: 180 seconds (6 update cycles). If no update is received for a route within 180 seconds, its metric is set to 16 (unreachable).
120. **What happened at $t = 200\text{s}$ when the link between Router 1 and Router 2 failed?**
     - _Answer_: The physical interface went down, triggering an immediate RIP route table update and causing Router 1 and Router 2 to advertise the severed link with metric 16.
121. **What is a `.gdf` file in OPNET?**
     - _Answer_: A Generic Data File exported by OPNET containing auto-assigned IP addresses, subnet masks, and interface mappings across the simulated topology.
122. **What is the maximum hop count metric in RIP and why?**
     - _Answer_: 15 hops. Hop count 16 signifies infinity/unreachable, preventing packets from looping indefinitely during count-to-infinity convergence cycles.
123. **Why does `Drop_Fast` achieve higher average throughput than `Drop_NoFast`?**
     - _Answer_: Because `Drop_Fast` avoids entering the slow start penalty phase ($cwnd=1$), recovering from lost packets while maintaining a large pipeline window.
124. **What are the three hierarchical modeling levels in OPNET?**
     - _Answer_:
       1. Network Domain (Topologies, nodes, and links)
       2. Node Domain (Internal protocol modules, queues, and processors)
       3. Process Domain (Finite State Machines written in Proto-C).
125. **What is an Application Configuration object in OPNET?**
     - _Answer_: A top-level network object used to define available network services (e.g. Email, HTTP, High Load FTP, Video Streaming) and their operational parameters.
126. **What is a Profile Configuration object in OPNET?**
     - _Answer_: An object that specifies user behavioral patterns, start times, and activity durations for the applications defined in the Application Configuration.

---

## Exercise 10: Cisco Packet Tracer Network Design

127. **What is Cisco Packet Tracer?**
     - _Answer_: Cisco Packet Tracer is a visual network simulation and visualization software designed by Cisco Systems to model, configure, and troubleshoot network devices and protocols.
128. **What is the function of the WPC300N module?**
     - _Answer_: It is a 2.4 GHz 802.11b/g/n wireless network adapter module installed in PC/laptop slots to enable Wi-Fi network connectivity.
129. **What is SSID in wireless networking?**
     - _Answer_: Service Set Identifier (SSID) is a 32-character alphanumeric name that uniquely identifies a specific wireless local area network (WLAN).
130. **Explain the 4-step DHCP DORA process.**
     - _Answer_:
       1. **D**iscover: Client broadcasts looking for DHCP servers.
       2. **O**ffer: Server offers an available IP address lease.
       3. **R**equest: Client requests the offered IP.
       4. **A**cknowledge: Server confirms the IP lease assignment.
131. **What is the purpose of an A Record in a DNS Server?**
     - _Answer_: An Address (A) record maps a domain name (e.g. `Cisco.com`) to a corresponding 32-bit IPv4 address (`208.67.220.220`).
132. **What is the difference between Straight-Through and Cross-Over copper cables?**
     - _Answer_: Straight-through cables connect dissimilar devices (e.g., PC to Switch/Router LAN port), while cross-over cables connect similar devices (e.g., PC to PC, Switch to Switch).
133. **What is the role of the Cable Modem in this topology?**
     - _Answer_: It modulates and demodulates Ethernet data signals into radio frequency (RF) signals transmitted across coaxial cables to the ISP head-end.
134. **What does the Default Gateway represent to an end device?**
     - _Answer_: The default gateway is the Layer-3 router IP address that an end device forwards packets to whenever the destination IP address lies outside its local subnet.
135. **What is NAT and why is it needed on the Wireless Router?**
     - _Answer_: Network Address Translation converts multiple private RFC 1918 IP addresses (e.g. `192.168.0.x`) into a single public routable IP address for internet access, conserving public IPv4 space.
136. **What does `ipconfig /renew` do?**
     - _Answer_: It forces the client's network adapter to send a fresh DHCP Request message to the DHCP server to update or renew its assigned IP lease.
137. **Why does the first packet in a ping test often time out?**
     - _Answer_: The first packet is delayed or dropped while intermediate devices resolve the destination MAC address using the Address Resolution Protocol (ARP).
138. **What is the significance of the IP address `208.67.220.220`?**
     - _Answer_: In the real world, `208.67.220.220` is one of Cisco's OpenDNS public nameserver addresses, modeled here as the enterprise web/DNS server.
139. **What is the difference between Logical and Physical Workspace in Packet Tracer?**
     - _Answer_: The Logical Workspace allows designing network topology diagrams and logical connections; the Physical Workspace models real-world physical layouts (cities, buildings, racks, and wiring closets).
140. **What is Simulation Mode vs Realtime Mode in Packet Tracer?**
     - _Answer_: Realtime mode simulates network events with actual clock speed; Simulation mode lets you step through packet flow packet-by-packet, inspecting PDU headers at each OSI layer.
