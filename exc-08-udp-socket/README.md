# Exercise 8: Client-Server UDP Application Using Socket Programming

## 1. Experiment Number & Title
- **Experiment No**: 8
- **Manual Title**: Develop a client-server based UDP application using socket programming functions.
- **Course**: 24IT5611 – Networks Laboratory (Academic Year 2026–2027 Odd)

---

## 2. Aim
To write a Java program for implementing **Client–Server communication using UDP Sockets**, where the client sends a connectionless datagram to the server and the server receives and acknowledges it.

---

## 3. How to Run
- Terminal 1: `run_server.bat` (or `.un_server.ps1`)
- Terminal 2: `run_client.bat` (or `.un_client.ps1`)

---

## 4. Sample Input & Output
- Client input: `Hello Server`
- Server receives: `Message from Client: Hello Server`
- Client receives: `Server Response: Message received successfully`

---

## 5. Viva Questions & Answers (14 Questions)
1. **What is UDP?** User Datagram Protocol (RFC 768); connectionless, best-effort message-oriented protocol.
2. **UDP header size?** 8 bytes (SrcPort, DstPort, Length, Checksum).
3. **TCP vs UDP?** TCP = connection-oriented, reliable, heavy header (20B); UDP = connectionless, low overhead (8B).
4. **Why real-time apps use UDP?** Prioritizes low latency over 100% reliability; retransmission causes lag.
5. **Java UDP classes?** `DatagramSocket` and `DatagramPacket`.
6. **How does server know where to reply?** From packet headers via `dpReceive.getAddress()` and `dpReceive.getPort()`.
7. **Message framing in UDP?** Discrete datagrams preserve exact packet boundaries.
8. **What if UDP > MTU?** Fragmented at IP layer; loss of any fragment drops whole packet.
9. **Is UDP checksum mandatory?** Optional in IPv4; mandatory in IPv6.
10. **Can UDP multicast?** Yes; natively supports broadcast and multicast.
11. **What is an ephemeral port?** Short-lived port dynamically assigned by OS to client.
12. **What does setSoTimeout() do?** Prevents client from freezing indefinitely if server is offline.
13. **Why use dpReceive.getLength()?** Decodes actual received payload bytes rather than entire buffer.
14. **Why DNS uses UDP?** Avoids handshake overhead for single-packet queries.

---

## 6. Result Statement
Thus, the Java program for implementing Client–Server based UDP application using Socket programming was executed and verified successfully.
