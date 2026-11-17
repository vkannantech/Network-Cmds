# Q5 – Custom DNS Resolver over UDP

## Question
> A custom DNS resolver must handle **domain-to-IP queries using UDP**. Design the system to **respond to valid requests and handle errors**. Discuss how the **stateless nature of UDP is managed** and how **resolution performance can be improved**.

## Aim
To implement a DNS server and resolver client using UDP datagram sockets. It supports forward (name → IP) and reverse (IP → name) lookups, returns proper error codes, and uses query IDs, timeout/retry and a TTL cache.

## Apparatus Required
Java JDK 8 or later. Files: `DNSServer.java`, `DNSClient.java`.

## Records in the server
| Domain | IP |
|---|---|
| www.google.com | 142.250.183.4 |
| www.aptitudeguru.com | 165.165.80.80 |
| www.downloadcyclone.blogspot.com | 165.165.79.1 |
| www.college.edu | 10.10.1.5 |
| mail.college.edu | 10.10.1.6 |
| lab.college.edu | 10.10.2.10 |

## Message Format
- Request: `id|query`
- Responses:
  - `id|NOERROR|answer|ttl` – found
  - `id|NXDOMAIN|query` – name doesn't exist
  - `id|FORMERR|reason` – bad name, bad IP, or malformed packet

(Like real DNS: RCODE 0 = NOERROR, 3 = NXDOMAIN, 1 = FORMERR.)

## Algorithm
**Server**
1. Create `DatagramSocket(5300)` and load the A records (name → IP) and PTR records (IP → name) into HashMaps.
2. Loop: `receive()` a datagram (512-byte buffer, like DNS) and split it into `id` and `query`.
3. Bad format → FORMERR. Query looks like an IP → validate octets ≤ 255, then reverse lookup. Otherwise validate the domain syntax, then forward lookup.
4. Found → NOERROR + answer + TTL, else NXDOMAIN. Send the reply to the sender's address and port, echoing the same `id`.

**Client**
1. Read a name or IP. **Check the local cache first.** If it's present and the TTL hasn't expired, answer immediately.
2. Otherwise build `id|query` with a new id and send it. Wait 2 s; on timeout **retry** (3 attempts).
3. Accept only a reply whose id matches (ignore stale replies).
4. NOERROR → print it and store it in the cache with its TTL. NXDOMAIN/FORMERR → print the error.

## Flow chart
```
 Client                                    Server (UDP 5300)
 query --> in cache & not expired? --yes--> print "from CACHE"
               | no
 send "id|query" -----------------------> receive()
 wait 2 s (retry x3)                      validate -> FORMERR
                                          lookup  -> NOERROR / NXDOMAIN
 reply id == my id? <------------------- send "id|status|answer|ttl"
   | yes
 print + cache with TTL
```

## How to Run
| | Windows | Linux |
|---|---|---|
| Server | `run_server.bat` | `./run_server.sh` |
| Client | `run_client.bat` | `./run_client.sh` |

In the client, type: `www.google.com`, `10.10.1.5`, `www.google.com` (again, now cached), `www.unknown.com`, `bad..name`, `cache`, `exit`.

## Sample Output (verified)
**Client**
```
Enter DOMAIN NAME or IP (cache / exit): www.google.com
Answer: 142.250.183.4   [from SERVER, id=686, TTL=60s, 15.156 ms]
Enter DOMAIN NAME or IP (cache / exit): 10.10.1.5
Answer: www.college.edu   [from SERVER, id=687, TTL=60s, 5.280 ms]
Enter DOMAIN NAME or IP (cache / exit): www.google.com
Answer: 142.250.183.4   [from CACHE, 0.008 ms]
Enter DOMAIN NAME or IP (cache / exit): www.unknown.com
Error NXDOMAIN: 'www.unknown.com' does not exist
Enter DOMAIN NAME or IP (cache / exit): bad..name
Error FORMERR: invalid domain name
Enter DOMAIN NAME or IP (cache / exit): 999.1.1.1
Error FORMERR: invalid IP address
Enter DOMAIN NAME or IP (cache / exit): cache
  www.google.com -> 142.250.183.4  (expires in 59 s)
  10.10.1.5 -> www.college.edu  (expires in 59 s)
```
**Server**
```
DNS Server listening on UDP port 5300 with 6 records
#1 127.0.0.1:42094  query="686|www.google.com"  reply="686|NOERROR|142.250.183.4|60"  (13.945 ms)
#2 127.0.0.1:42094  query="687|10.10.1.5"  reply="687|NOERROR|www.college.edu|60"  (0.128 ms)
#3 127.0.0.1:42094  query="688|www.unknown.com"  reply="688|NXDOMAIN|www.unknown.com"  (0.159 ms)
#4 127.0.0.1:42094  query="689|bad..name"  reply="689|FORMERR|invalid domain name"  (0.135 ms)
#5 127.0.0.1:42094  query="690|999.1.1.1"  reply="690|FORMERR|invalid IP address"  (0.082 ms)
```
**Server down (error handling)**
```
  timeout (attempt 1/3), retrying...
  timeout (attempt 2/3), retrying...
  timeout (attempt 3/3)
Error: DNS server not responding
```
Note that the second `www.google.com` came from the cache in **0.008 ms** vs about 5–15 ms from the server.

## Discussion (answer to "Discuss")
**Managing UDP's stateless nature**
- UDP has no connection, so **each request is self-contained**: it carries an **ID** and the full query, and the server keeps no per-client state. It just answers whoever sent the datagram (`getAddress()`, `getPort()`).
- **Query ID matching:** the client accepts only the reply with its own ID, so a late reply to an old query can't be mistaken for the current answer (real DNS uses a 16-bit ID for the same reason, and to resist spoofing).
- **Timeout + retry:** UDP may lose packets, so the client waits 2 s and resends up to 3 times, then reports the server unreachable instead of hanging.
- **Idempotent queries:** asking twice gives the same answer, so retransmitting is always safe.
- Because the server is stateless, it serves **any number of clients** with a single socket and no threads.

**Error handling:** FORMERR for malformed packets, invalid names (RFC 1035 label syntax) and invalid IPs; NXDOMAIN for unknown names; 512-byte message limit; the client handles timeouts.

**Improving resolution performance**
1. **Client-side caching with TTL** (implemented): repeat lookups take microseconds and need no network traffic.
2. **HashMap lookups**: O(1) instead of scanning arrays.
3. **UDP instead of TCP**: one request and one reply, no handshake.
4. Server-side caching of upstream answers, negative caching of NXDOMAIN, replicated/anycast servers, and prefetching popular names before their TTL expires.

## Viva Questions
1. **Why does DNS use UDP?** Queries are small and a single request/response is enough; UDP avoids TCP's handshake. TCP is used for large replies and zone transfers.
2. **Port of DNS?** 53. This lab uses 5300 because ports below 1024 need admin rights.
3. **What is TTL in DNS?** How long a resolver may cache the answer.
4. **What is NXDOMAIN?** A response meaning the domain does not exist.
5. **What is a reverse lookup?** IP → name, using PTR records (in-addr.arpa).
6. **Recursive vs iterative query?** Recursive: the server finds the full answer for you. Iterative: it refers you to another server.

## Result
A custom DNS resolver over UDP was implemented. Forward and reverse lookups returned correct answers, errors were reported as NXDOMAIN/FORMERR, query IDs and timeout/retry handled UDP's statelessness, and TTL caching improved resolution time.
