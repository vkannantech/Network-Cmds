# Exercise 2: Implementation of HTTP and DNS Protocols

## 1. Experiment Number & Title
- **Experiment No**: 2
- **Manual Title**: Implementation of HTTP and DNS protocols.
- **Course**: Networks-Commands-Deeply Engineering Suite

---

## 2. Aim
To write Java programs for implementing the HTTP protocol using TCP sockets for reliable binary image transfer and the DNS protocol using UDP sockets for forward and reverse domain/IP address resolution.

---

## 3. Requirements & Software
- **Operating System**: Windows 10 / Windows 11
- **Software**: Java Development Kit (JDK 8 or higher)
- **Editor / Shell**: Command Prompt, PowerShell, or any Java IDE
- **Assets**: Image file `network.jpg` in the local working directory

---

## 4. Concepts Covered
- **HTTP via TCP Stream Sockets (`Socket` / `ServerSocket`)**:
  - Reliable connection-oriented byte streaming.
  - Image serialization into byte arrays via `ByteArrayOutputStream` and `ImageIO`.
  - Prefixing payload length (`DataOutputStream.writeInt`) to prevent truncation and buffer overruns.
  - Graphical image rendering in Java Swing (`JFrame`, `JLabel`, `ImageIcon`).
- **DNS via UDP Datagram Sockets (`DatagramSocket` / `DatagramPacket`)**:
  - Connectionless, low-latency request-response messaging.
  - Forward DNS resolution (Hostname -> IP address).
  - Reverse DNS lookup (IP address -> Hostname).
  - Static memory lookup table simulation with Datagram packet encoding.

---

## 5. Manual-Derived Algorithm / Procedure

### A. HTTP Image Transfer (TCP Sockets)
1. Start the program.
2. Start Server (`Serverhttp`) on TCP port `387`.
3. Connect Client (`Clienthttp`) to `localhost:387`.
4. Client reads `network.jpg`, writes length and byte array.
5. Server reads length via `readInt()`, reads byte array via `readFully()`, and renders image in Swing JFrame.
6. Close streams and sockets.

### B. DNS Address Resolution (UDP Sockets)
1. Start the program.
2. Start Server (`Serverdns12`) on UDP port `1309`.
3. Client (`Clientdns12`) inputs domain or IP, sends DatagramPacket.
4. Server scans database table:
   - `www.aptitudeguru.com` <-> `165.165.80.80`
   - `www.downloadcyclone.blogspot.com` <-> `165.165.79.1`
5. Returns matched answer packet to client; client prints result.

---

## 6. Files in this Exercise
- [Clienthttp.java](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/Clienthttp.java)
- [Serverhttp.java](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/Serverhttp.java)
- [Clientdns12.java](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/Clientdns12.java)
- [Serverdns12.java](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/Serverdns12.java)
- [network.jpg](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/network.jpg)
- [compile.bat](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/compile.bat) / [compile.ps1](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/compile.ps1)
- [run_http_server.bat](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/run_http_server.bat) / [run_http_client.bat](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/run_http_client.bat)
- [run_dns_server.bat](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/run_dns_server.bat) / [run_dns_client.bat](file:///d:/Collage%20Meterials/SEM-5/DCCN/Network_Lab_Manual/network-lab/exc-02-http-dns/run_dns_client.bat)

---

## 7. How to Run on Windows CMD / PowerShell

### HTTP Test:
- Terminal 1: `run_http_server.bat` (or `.
un_http_server.ps1`)
- Terminal 2: `run_http_client.bat` (or `.
un_http_client.ps1`)

### DNS Test:
- Terminal 1: `run_dns_server.bat` (or `.
un_dns_server.ps1`)
- Terminal 2: `run_dns_client.bat` (or `.
un_dns_client.ps1`)

---

## 8. Sample Input & Expected Output
- **HTTP**: Transmits `network.jpg` (renders in JFrame popup).
- **DNS**: `www.aptitudeguru.com` -> `165.165.80.80`; `165.165.79.1` -> `www.downloadcyclone.blogspot.com`.

---

## 9. Viva Questions & Answers (14 Questions)
1. **Why does HTTP use TCP rather than UDP?** Reliable stream transmission ensures web content is not corrupted.
2. **Why does DNS primarily use UDP?** DNS requests are single small packets; UDP avoids 3-way handshake overhead.
3. **When does DNS use TCP?** When response payload > 512 bytes or during Zone Transfers (AXFR).
4. **What is the standard port for HTTP and DNS?** HTTP: TCP 80; DNS: UDP/TCP 53 (Manual uses 387 and 1309 for lab isolation).
5. **What is the role of ByteArrayOutputStream?** Serializes BufferedImage into byte buffer to calculate byte length.
6. **Why send image length before image data?** TCP is a byte stream without delimiters; length enables `readFully()`.
7. **What is forward vs reverse DNS?** Forward: Name -> IP; Reverse: IP -> Name.
8. **What is a Datagram in UDP?** Independent packet containing destination coordinates and payload.
9. **What does readFully() do?** Blocks until the entire byte array buffer is completely filled.
10. **What is InetAddress.getByName()?** Resolves hostname/IP string to Java Layer-3 network address object.
11. **Why flush the stream?** Pushes buffered bytes immediately through the network channel.
12. **Can UDP handle multiple clients?** Yes; connectionless DatagramSocket receives from any sender and replies using packet coordinates.
13. **What is iterative vs recursive DNS?** Recursive queries resolve the full path; iterative queries return referrals.
14. **What buffer size is used for DNS?** 1024 bytes (`new byte[1024]`).

---

## 10. Result Statement
Thus, the Java programs for implementing HTTP protocol using TCP sockets for image transfer and DNS protocol using UDP sockets for domain name and IP address resolution were written, executed, and verified successfully.
