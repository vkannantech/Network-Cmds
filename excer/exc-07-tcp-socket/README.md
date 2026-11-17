# Exercise 7: Client-Server TCP Application Using Socket Programming

## 1. Experiment Number & Title
- **Experiment No**: 7
- **Manual Title**: Develop a client-server based TCP application using socket programming functions.
- **Course**: Networks-Commands-Deeply Engineering Suite

---

## 2. Aim
To write a Java program for implementing **Client–Server communication using TCP Sockets**, where the client sends a message to the server and the server receives and acknowledges with a response.

---

## 3. How to Run
- Terminal 1: `run_server.bat` (or `.
un_server.ps1`)
- Terminal 2: `run_client.bat` (or `.
un_client.ps1`)

---

## 4. Sample Input & Output
- Client input: `Hello Server`
- Server receives: `Message from Client: Hello Server`
- Client receives: `Server Response: Message received successfully`

---

## 5. Viva Questions & Answers (14 Questions)
1. **What is a Socket?** Endpoint for bidirectional communication identified by IP, protocol, and port.
2. **Explain 3-way handshake?** SYN -> SYN-ACK -> ACK.
3. **ServerSocket vs Socket?** ServerSocket listens for inbound connections; Socket communicates on established connection.
4. **Why is TCP reliable?** Positive ACKs, sequence numbers, checksums, and automatic retransmissions.
5. **What does accept() do?** Blocks until client connects, returning a new dedicated Socket.
6. **What does readUTF() do?** Decodes length-prefixed UTF-8 string automatically framing messages.
7. **How does TCP do flow control?** Receiver advertises Receive Window (rwnd) in headers.
8. **How does TCP do congestion control?** Adjusts Congestion Window (cwnd) via AIMD.
9. **Explain 4-way termination?** FIN -> ACK -> FIN -> ACK.
10. **Why TIME_WAIT state?** Ensures final ACK delivery and allows stale duplicate packets to die out.
11. **Can multiple clients use same server port?** Yes; differentiated by (SrcIP, SrcPort, DstIP, DstPort).
12. **Port ranges?** Well-known (0-1023), Registered (1024-49151), Dynamic (49152-65535).
13. **Stream vs Datagram socket?** Stream = reliable ordered byte stream; Datagram = discrete unreliable packets.
14. **What does flush() do?** Forces immediate transmission of buffered output bytes.

---

## 6. Result Statement
Thus, the Java program for implementing Client–Server based TCP application using Socket programming was executed and verified successfully.
