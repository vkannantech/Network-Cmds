# Q3 – Reliable Real-Time Quiz over UDP

## Question
> A real-time quiz system using UDP shows **message loss and duplication**. Modify the design to **improve reliability without affecting speed**. Discuss how **error handling, acknowledgments, and duplicate detection** can balance performance with data accuracy.

## Aim
To build a UDP quiz server and client that keep UDP's speed but add reliability at the application level: **sequence numbers, acknowledgements, timeout-based retransmission and duplicate detection**. The packet loss is simulated so the mechanisms can be seen working.

## Apparatus Required
Java JDK 8 or later. Files: `QuizServer.java`, `QuizClient.java`.

## Message Format
| Message | Direction | Meaning |
|---|---|---|
| `JOIN` | client → server | player joins |
| `Q|seq|text` | server → client | question number *seq* |
| `ACK|seq` | client → server | question *seq* received |
| `ANS|seq|answer` | client → server | answer to question *seq* |
| `AACK|seq|result` | server → client | answer received + Correct/Wrong |
| `END|score` | server → client | quiz over (sent 3 times) |

## Algorithm
**Server**
1. Create `DatagramSocket(7000)`; wait for `JOIN`. Set a 1000 ms receive timeout.
2. For each question *seq*:
   1. Send `Q|seq`. Wait up to 1 s for `ACK|seq` (an `ANS|seq` also counts as an ACK).
   2. On timeout, **retransmit** the same question (up to 60 tries).
   3. Wait for `ANS|seq`. If *seq* is already answered, it's a **duplicate**: re-send the AACK but **don't score it again**. Otherwise score it and send `AACK|seq|result`.
3. Send `END|score` (3 copies, since it is never ACKed) and print the statistics.

**Client**
1. Send `JOIN` until the first question arrives.
2. On `Q|seq`: always send `ACK|seq`. If `seq <= lastSeq` it's a **duplicate**: discard it, don't ask again. Otherwise show it and read the answer.
3. Send `ANS|seq|answer` and retransmit on timeout until `AACK|seq` arrives (or the server moves on to the next question).
4. On `END`, display the score and stop.

## Flow chart (stop-and-wait per message)
```
 Server                         Client
 send Q|1  ----X (lost)
 timeout -> resend Q|1 ------->  new seq: show question
           <------------------  ACK|1
           <------------------  ANS|1|b
 score, send AACK|1 ---X (lost)
           <------------------  (timeout) ANS|1|b  again
 seq 1 already answered -> DUPLICATE, not scored again
 send AACK|1 ----------------->  "Correct"
```

## How to Run
Start the server first, then the client. The optional argument is the simulated loss probability.

| | Windows | Linux |
|---|---|---|
| Server, no loss | `run_server.bat` | `./run_server.sh` |
| Server, 30% loss | `run_server.bat 0.3` | `./run_server.sh 0.3` |
| Client | `run_client.bat localhost 0.3` | `./run_client.sh localhost 0.3` |

Answer with `a`, `b` or `c`.

## Sample Output (verified, 30% simulated loss on both sides)
**Client**
```
   [SIMULATED LOSS] dropped: ACK|1
Q1. Which layer does TCP work in? (a) Network (b) Transport (c) Data Link
Your answer: b
   Timeout -> retransmit answer 1
   Server: Correct
Q2. Port number of HTTP? (a) 21 (b) 25 (c) 80
Your answer: c
   Server: Correct
Q3. UDP is (a) connection-oriented (b) connectionless
Your answer: a
   [SIMULATED LOSS] dropped: ANS|3|a
   Timeout -> retransmit answer 3
   Server: Wrong, answer is b
Q4. ARP maps (a) IP->MAC (b) MAC->IP (c) Name->IP
Your answer: a
   Server: Correct
*** Quiz finished. Your score: 3/4 ***
```
**Server**
```
Quiz Server on UDP 7000 (simulated loss = 30%)
Player joined from /127.0.0.1:50003
Sending Q1
   Answer for Q1 = b -> Correct
   [SIMULATED LOSS] dropped: AACK|1|Correct
Sending Q2
   [SIMULATED LOSS] dropped: Q|2|Port number of HTTP? (a) 21 (b) 25 (c) 80
   DUPLICATE answer for Q1 detected -> not scored again, re-sending AACK
   Timeout -> retransmit Q2 (try 2)
   [SIMULATED LOSS] dropped: Q|2|Port number of HTTP? (a) 21 (b) 25 (c) 80
   Timeout -> retransmit Q2 (try 3)
   ACK 2 received
   Answer for Q2 = c -> Correct
...
   [SIMULATED LOSS] dropped: END|3/4
   [SIMULATED LOSS] dropped: END|3/4
Quiz over. Score = 3/4
Retransmissions = 2, duplicates detected = 1
```
Even with 30% of packets dropped, every question arrived, every answer was scored **exactly once**, and the score is correct.

## Discussion (answer to "Discuss")
| Problem in plain UDP | Mechanism added | How it works |
|---|---|---|
| **Message loss** | ACK + timeout + retransmission | Each message waits for an ACK; if none arrives within 1 s it is resent |
| **Duplication** (from retransmits) | Sequence numbers + duplicate detection | The receiver remembers which *seq* it has handled; repeats are ACKed again but not processed twice (no double scoring) |
| **Lost ACK** | Re-ACK duplicates; an answer counts as an implicit ACK | The sender stops retransmitting as soon as it has any proof of delivery |
| **Peer crash / no reply** | Error handling: retry limit, `SocketTimeoutException` | After too many retries the quiz aborts with a clear message instead of hanging |
| **Malformed packets** | Parsing with `split("\\|")`, unknown types ignored | Bad packets can't crash the program |

**Why it stays fast:**
- No connection setup (no 3-way handshake like TCP); the first question goes out immediately.
- With no loss there are **zero** retransmissions: overhead is only one small ACK per message.
- The timeout (1 s) is short, so recovery is quick. Only the lost packet is resent, not a whole stream.
- No head-of-line blocking from a big TCP buffer, and no congestion-control slow-start.

**Balance:** data that must be accurate (answers, score) gets ACK + retransmit + dedupe. A real-time game could leave purely cosmetic updates (such as a timer display) unreliable. Making only the important messages reliable is the trade-off between performance and accuracy.

## Viva Questions
1. **Why is UDP faster than TCP?** No connection setup, no ACK/retransmission, no congestion control, and smaller headers (8 bytes vs 20).
2. **What is stop-and-wait ARQ?** Send one message and wait for its ACK before sending the next; retransmit on timeout.
3. **Why are sequence numbers needed?** To tell a new message from a retransmitted duplicate, and to match each ACK to its message.
4. **What happens if an ACK is lost?** The sender times out and resends. The receiver sees a duplicate *seq*, discards it, and ACKs again.
5. **How are duplicates prevented from changing the score?** The server keeps a set of answered *seq* numbers and scores each one only once.
6. **Choosing the timeout?** Slightly larger than the round-trip time. Too small causes needless resends; too large makes recovery slow.

## Result
A reliable UDP quiz system was implemented. Sequence numbers, acknowledgements, timeout retransmission and duplicate detection kept every question and answer delivered exactly once under 30% simulated packet loss, while keeping UDP's low latency.
