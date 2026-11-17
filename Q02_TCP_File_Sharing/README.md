# Q2 – Multi-User File Sharing System Using TCP Sockets

## Question
> A reliable file-sharing system is needed between departments. Using UNIX sockets and TCP, design a client-server model **supporting multiple users** with **reliable, ordered delivery**. Explain how the system ensures **connection stability, data integrity, and scalability**.

## Aim
To develop a multi-client file-sharing server and client using TCP stream sockets, where many departments can list, download and upload files at the same time, with checksum-verified integrity.

## Apparatus Required
Java JDK 8 or later, Windows or Linux PC. Files: `FileServer.java`, `FileClient.java`, `shared/` folder (files offered by the server).

## Protocol Design
| Client sends | Server replies |
|---|---|
| `LIST` | names of files in `shared/` |
| `GET <file>` | `OK`, file size (8 bytes), file bytes, CRC32 checksum — or `ERROR File not found` |
| `PUT <file>` + size + bytes + CRC32 | `OK Stored …` or `ERROR Checksum mismatch` |
| `QUIT` | `BYE` and connection closes |

## Algorithm
**Server**
1. Start. Create `ServerSocket` on port 6000 and a thread pool of 10 threads.
2. Loop: `accept()` a client and hand the socket to a pool thread (`ClientHandler`).
3. In the handler: set a read timeout and keep-alive, then loop reading commands with `readUTF()`.
4. `LIST` → send file names. `GET` → read the file, send size + bytes + CRC32. `PUT` → receive size + bytes + CRC32, recompute the CRC, and store the file only if it matches (writes are `synchronized`).
5. On `QUIT`, EOF or error, close the socket. The thread returns to the pool.

**Client**
1. Start. Connect to `server:6000` (5 s timeout).
2. Read a command from the user and send it.
3. For `GET`, receive size, bytes and CRC; recompute the CRC and save to `downloads/` only if it matches. For `PUT`, send the file with its CRC.
4. Repeat until `QUIT`, then close the socket.

## Flow chart
```
 SERVER                                   CLIENT
 ServerSocket(6000)                       Socket(host,6000)
      |                                         |
 accept() <----------- TCP 3-way handshake -----+
      |                                         |
 new thread from pool                     user types command
      |                                         |
 readUTF(cmd) <-------------- LIST/GET/PUT -----+
      |                                         |
 process + CRC32 ---------- data + checksum --->  verify CRC32
      |                                         |
 loop until QUIT ------------- BYE ------------>  close()
```

## How to Run
Use **two terminals**, and start the server first.

| | Windows | Linux |
|---|---|---|
| Server | double-click `run_server.bat` | `./run_server.sh` |
| Client | double-click `run_client.bat` (open 2–3 for multiple users) | `./run_client.sh` (in more terminals) |
| Client on another PC | `run_client.bat 192.168.1.5` | `./run_client.sh 192.168.1.5` |

Then type: `LIST`, `GET notice.txt`, `PUT somefile.txt`, `QUIT`

## Sample Output (verified, 2 clients at the same time)
**Client doing LIST / GET / PUT (shown as Client-2 in the server log)**
```
Connected to File Server localhost:6000
Command (LIST / GET <file> / PUT <file> / QUIT): LIST
Server:
notice.txt
timetable.txt
Command: GET notice.txt
Downloaded notice.txt (76 bytes) -> downloads/notice.txt
Integrity check: CRC32 cc82ab4 MATCHED
Command: GET nothere.txt
Server: ERROR File not found
Command: PUT report.txt
Server: OK Stored report.txt (21 bytes, checksum verified)
Command: QUIT
Server: BYE
Connection closed.
```
**Server**
```
File Server started on TCP port 6000, sharing folder: .../shared
[+] Connected   Client-1 /127.0.0.1:58614  (thread pool-1-thread-1)
[+] Connected   Client-2 /127.0.0.1:58622  (thread pool-1-thread-2)
    Client-1 /127.0.0.1:58614 -> GET timetable.txt
    Client-2 /127.0.0.1:58622 -> LIST
    sent timetable.txt (35 bytes, CRC32=9431208a)
    Client-2 /127.0.0.1:58622 -> GET notice.txt
    sent notice.txt (76 bytes, CRC32=cc82ab4)
    Client-1 /127.0.0.1:58614 -> QUIT
[-] Disconnected Client-1 /127.0.0.1:58614
    Client-2 /127.0.0.1:58622 -> PUT report.txt
    received report.txt (21 bytes) checksum OK
[-] Disconnected Client-2 /127.0.0.1:58622
```
The two clients' requests are interleaved, which shows they really are served at the same time on separate threads.

## Explanation (answer to "Explain")
**Reliable, ordered delivery** – TCP gives this automatically: sequence numbers, acknowledgements, retransmission of lost segments, and in-order reassembly. `readFully()` waits until every byte of the file has arrived.

**Connection stability**
- Connection-oriented: TCP 3-way handshake before data, FIN on close.
- `setKeepAlive(true)` detects dead peers; `setSoTimeout()` frees threads held by idle or crashed clients.
- Every client is handled inside try/finally, so one failing client never crashes the server or other users.
- The client uses a connect timeout, so it fails cleanly if the server is down.

**Data integrity**
- TCP's own checksum catches transmission errors.
- An application-level **CRC32 of the whole file** is sent with it and checked on the other side, catching corruption that TCP can't (disk or buffer errors). A mismatched file is rejected.
- File names are reduced to their base name (`new File(name).getName()`), which blocks `../` path tricks.
- Concurrent uploads are `synchronized`, so two users can't corrupt the same file.

**Scalability**
- **Thread pool** (`Executors.newFixedThreadPool(10)`): many users at once, with a bounded number of threads so the server isn't overloaded. Extra clients wait in the queue.
- A simple text command protocol makes it easy to add commands (DELETE, RENAME…).
- For more users: raise the pool size, run several servers behind a load balancer, or use NIO non-blocking sockets.

## Viva Questions
1. **Why TCP and not UDP for file transfer?** Files must arrive complete and in order. TCP guarantees this; UDP does not.
2. **What does `accept()` do?** It blocks until a client connects, then returns a new `Socket` for that client.
3. **Why a thread pool rather than one thread per client?** It limits resource use. Unlimited threads can exhaust memory under heavy load.
4. **Why send the file size first?** TCP is a byte stream with no message boundaries; the size tells the receiver how many bytes belong to the file.
5. **What is CRC32?** A 32-bit cyclic redundancy check used to detect corrupted data.
6. **Socket API calls (UNIX names):** `socket()`, `bind()`, `listen()`, `accept()` on the server; `socket()`, `connect()` on the client; then `read()`/`write()`, `close()`.

## Result
A multi-user TCP file-sharing system was developed and executed. Several clients listed, downloaded and uploaded files at the same time, with reliable ordered delivery and CRC32-verified integrity.
