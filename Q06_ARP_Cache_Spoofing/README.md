# Q6 – ARP Request/Reply, Caching and Spoofing Protection

## Question
> In a LAN simulation, devices need to resolve MAC addresses. Implement **ARP request/reply and caching**, and **handle spoofing risks**. Explain how the design **reflects real ARP behavior** and supports **secure address resolution**.

## Aim
To simulate ARP on a LAN: broadcast request, unicast reply, an ARP cache with aging (TTL), static entries, and detection or blocking of ARP spoofing (cache poisoning).

## Apparatus Required
Java JDK 8 or later. File: `ARPSimulator.java`.

## Simulated LAN (192.168.1.0/24)
| Host | IP | MAC |
|---|---|---|
| **This host** | 192.168.1.10 | AA:AA:AA:00:00:10 |
| Gateway (static entry) | 192.168.1.1 | 00:1A:2B:3C:4D:01 |
| PC2 | 192.168.1.20 | 00:1A:2B:3C:4D:20 |
| PC3 | 192.168.1.30 | 00:1A:2B:3C:4D:30 |
| File server | 192.168.1.50 | 00:1A:2B:3C:4D:50 |
| Attacker | 192.168.1.66 | DE:AD:BE:EF:00:66 |

## Algorithm
1. Start. Load the LAN hosts and pin the gateway as a **static** cache entry.
2. Show the menu: 1 Resolve, 2 Show cache, 3 Spoof attack, 4 Advance time, 5 Toggle security, 6 Exit.
3. **Resolve IP:**
   1. Remove expired entries (age ≥ 60 s).
   2. On a **cache hit**, return the MAC; no packet is sent.
   3. On a **cache miss**, broadcast an ARP REQUEST (`dst FF:FF:FF:FF:FF:FF`, "Who has X? Tell 192.168.1.10") and mark X as *pending*.
   4. Every host compares X to its own IP. Only the owner sends a unicast ARP REPLY "X is at MAC".
   5. No reply → "host unreachable".
4. **On receiving a reply (security ON):**
   - Unsolicited (no pending request) → **block**.
   - Conflicts with a **static** entry → **block**.
   - MAC differs from an existing entry → **alert**, ignore.
   - Otherwise store it as dynamic with a timestamp.
5. **Spoof attack:** the attacker sends a forged reply "victimIP is at DE:AD:BE:EF:00:66" and the defences above are applied. With security OFF, the cache gets poisoned.
6. **Aging:** advancing the clock removes dynamic entries older than the TTL. Static entries stay.

## Flow chart
```
 resolve(IP) -> cache hit? --yes--> use MAC (no traffic)
                   | no
     broadcast "Who has IP?" ---> all hosts check
                   |                 owner --> unicast reply "IP is at MAC"
     reply received
       solicited? --no--> BLOCK (spoof)
       static entry & different MAC? --yes--> BLOCK
       existing entry & MAC changed? --yes--> ALERT
                   | ok
     store in cache (dynamic, TTL 60 s)
```

## How to Run
| Windows | Linux |
|---|---|
| `run.bat` | `./run.sh` |

Suggested demo inputs: `1` → `192.168.1.20` (miss, request/reply) · `1` → `192.168.1.20` (hit) · `1` → `192.168.1.99` (unreachable) · `2` · `3` → `192.168.1.1` · `3` → `192.168.1.20` · `5` (security OFF) · `3` → `192.168.1.20` (poisoned!) · `2` · `4` → `70` · `2` · `6`

## Sample Output (verified, menu lines trimmed)
```
This host: 192.168.1.10 / AA:AA:AA:00:00:10
Enter your choice: 1
Enter IP Address: 192.168.1.20
CACHE MISS: sending ARP REQUEST (broadcast)
  [ARP REQUEST] src=AA:AA:AA:00:00:10 dst=FF:FF:FF:FF:FF:FF : Who has 192.168.1.20? Tell 192.168.1.10
    192.168.1.1 : not my IP, discard
    192.168.1.30 : not my IP, discard
    192.168.1.50 : not my IP, discard
    192.168.1.66 : not my IP, discard
    192.168.1.20 : that's me! (also caches 192.168.1.10 -> AA:AA:AA:00:00:10)
  [ARP REPLY] (unicast) 192.168.1.20 is at 00:1A:2B:3C:4D:20
  Cache updated: 192.168.1.20 -> 00:1A:2B:3C:4D:20 (TTL 60s)
Enter your choice: 1
Enter IP Address: 192.168.1.20
CACHE HIT : 192.168.1.20 -> 00:1A:2B:3C:4D:20 (dynamic)  - no packet sent
Enter your choice: 1
Enter IP Address: 192.168.1.99
  No ARP reply after 3 attempts -> Host 192.168.1.99 unreachable
Enter your choice: 2
Internet Address    Physical Address     Type      Age
192.168.1.1         00:1A:2B:3C:4D:01    static    -
192.168.1.20        00:1A:2B:3C:4D:20    dynamic   0s
Enter your choice: 3
IP the attacker wants to impersonate: 192.168.1.1
Attacker 192.168.1.66 sends forged ARP REPLY: "192.168.1.1 is at DE:AD:BE:EF:00:66"
  ** BLOCKED: unsolicited ARP reply (we never asked for 192.168.1.1) - ignored
  Cache safe: attack defeated
Enter your choice: 5
Security is now OFF
Enter your choice: 3
IP the attacker wants to impersonate: 192.168.1.20
  Cache updated: 192.168.1.20 -> DE:AD:BE:EF:00:66 (TTL 60s)
  !!! CACHE POISONED: traffic for 192.168.1.20 now goes to the attacker (man-in-the-middle)
Enter your choice: 4
Seconds to advance: 70
  Aging: entry 192.168.1.20 expired and removed
Enter your choice: 2
Internet Address    Physical Address     Type      Age
192.168.1.1         00:1A:2B:3C:4D:01    static    -
Enter your choice: 6
Program terminated
```

## Explanation (answer to "Explain")
**How it reflects real ARP behaviour**
| Real ARP | In this simulation |
|---|---|
| Request is a **broadcast** to FF:FF:FF:FF:FF:FF | Printed with `dst=FF:FF:FF:FF:FF:FF`, and every host examines it |
| Only the owner replies, by **unicast** | Only the matching host sends `[ARP REPLY] (unicast)` |
| Target also learns the sender's mapping | "also caches 192.168.1.10 -> …" |
| ARP cache avoids repeated broadcasts | Cache hit sends no packet |
| Dynamic entries age out (Windows ≈ 15–45 s, Linux ≈ 60 s) | TTL 60 s, removed by aging |
| Static entries (`arp -s`) never expire | Gateway pinned as static |
| No reply means the host is unreachable | "No ARP reply … unreachable" |

**ARP spoofing risk:** ARP has **no authentication**. Any host can send a reply saying "the gateway's IP is at my MAC". Victims that accept it send their traffic to the attacker (man-in-the-middle, sniffing, denial of service). This is shown by turning security OFF.

**Secure address resolution (defences implemented)**
1. **Reject unsolicited replies:** only accept a reply for an IP we actually requested (pending table).
2. **Static entries** for critical hosts (gateway, servers) can't be overwritten.
3. **Change detection:** if a known IP suddenly maps to a new MAC, raise an alert and keep the old entry (like the `arpwatch` tool).
4. **Aging** limits how long any bad entry can survive.
In real networks: **Dynamic ARP Inspection (DAI)** on switches, DHCP snooping, port security, and 802.1X.

## Viva Questions
1. **What does ARP do?** It maps an IPv4 address to a MAC address on the local network.
2. **Why is the ARP request broadcast but the reply unicast?** The sender doesn't yet know the target's MAC; the target does know the sender's MAC (it's in the request).
3. **What is gratuitous ARP?** An unsolicited ARP announcing your own IP/MAC, used for duplicate-IP detection and failover (and abused for spoofing).
4. **Command to see the ARP cache?** `arp -a` (Windows and Linux) or `ip neigh` (Linux).
5. **What is ARP poisoning?** Inserting a false IP→MAC mapping into a victim's cache.
6. **Which layers does ARP connect?** Layer 3 (IP) to Layer 2 (MAC).

## Result
ARP request/reply with caching, aging and static entries was simulated successfully. ARP spoofing was shown to poison the cache when unprotected, and was blocked by solicited-reply checking, static entries and MAC-change detection.
