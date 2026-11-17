# Q8 – Sliding Window (Go-Back-N) with CRC over an Unreliable Channel

## Question
> For communication over an unreliable channel, implement **sliding window with CRC**. Show **packet transfer, acknowledgments, and error checking**. Explain how the protocol ensures **reliability and efficiency** in data transmission.

## Aim
To simulate the Go-Back-N sliding window protocol with CRC error detection. Frames carry CRC bits, the channel randomly loses or corrupts frames and ACKs, the receiver checks the CRC, and the sender slides its window on cumulative ACKs and goes back N on timeout.

## Apparatus Required
Java JDK 8 or later. File: `SlidingWindowCRC.java`.

## Inputs
| Input | Example | Meaning |
|---|---|---|
| Number of frames | 8 | frames to deliver (each holds 8 random data bits) |
| Window size | 4 | max unacknowledged frames in flight |
| Channel error rate | 30 | % of frames hit: half lost, half bit-corrupted; ACK loss = rate / 3 |
| Generator | 1011 | CRC polynomial x³ + x + 1 → 3 CRC bits |

The random seed is fixed (42), so every run gives the same output. Change `new Random(42)` to vary it.

## Algorithm
**CRC (sender side)**
1. Append (generator length − 1) zeros to the data.
2. Do modulo-2 division (XOR) by the generator. The remainder is the CRC bits.
3. Transmitted codeword = data + CRC.

**CRC (receiver side):** divide the received codeword by the generator. Remainder all 0 → accept; otherwise **error detected**, discard the frame.

**Go-Back-N**
1. `base = 0`. Repeat while `base < n`:
2. Send every frame in the window `[base, base+W-1]`. Each one passes through the channel (may be lost, corrupted, or delivered).
3. Receiver: CRC fails → discard. Frame ≠ expected → discard and re-ACK the expected number. Otherwise accept, `expected++`, and ACK `expected` (cumulative).
4. The ACK may be lost. If an ACK > base arrives, **slide the window**: `base = ACK`.
5. If frames remain unacknowledged → **timeout → go back** and resend from `base`.
6. Print statistics: transmissions, retransmissions, losses, CRC errors, efficiency.

## Flow chart
```
 Sender window [base .. base+W-1]
        | send frames (data+CRC)
        v
   ~~~ channel: lost / bit flip / ok ~~~
        v
 Receiver: CRC remainder == 0 ? --no--> discard (error detected)
                | yes
          seq == expected ? --no--> discard, re-ACK expected
                | yes
          accept, ACK expected+1 (cumulative)
        v
 Sender: ACK > base ? --yes--> slide window
         frames left unACKed ? --yes--> TIMEOUT, Go-Back-N from base
```

## Worked CRC example (matches the lab manual)
Data `1001101`, generator `1011`:
```
1001101 000  ÷ 1011  (XOR division)  ->  remainder 101
Transmitted codeword = 1001101 101
```
(The lab manual prints `100`, which is a typo. `101` is mathematically correct, and this program gives `1001101101`.)

## How to Run
| Windows | Linux |
|---|---|
| `run.bat` | `./run.sh` |

Inputs: `8`, `4`, `30`, `1011`. Use error rate `0` to see an error-free run.

## Sample Output (verified: 8 frames, window 4, 30% errors, generator 1011)
```
--- CRC encoding (generator 1011) ---
Frame 0: data 10100101  CRC 101  -> transmitted 10100101101
Frame 1: data 10100001  CRC 010  -> transmitted 10100001010
Frame 2: data 01111000  CRC 010  -> transmitted 01111000010
Frame 3: data 11010010  CRC 000  -> transmitted 11010010000
Frame 4: data 00100010  CRC 100  -> transmitted 00100010100
Frame 5: data 11101011  CRC 101  -> transmitted 11101011101
Frame 6: data 00001001  CRC 110  -> transmitted 00001001110
Frame 7: data 10110100  CRC 111  -> transmitted 10110100111

=== Round 1 | window [0..3] ===
Sender  : frame 0 sent 10100101101
Receiver: frame 0 CRC OK -> accepted, data 10100101, send ACK 1
Sender  : frame 1 sent 10100001010
Receiver: frame 1 CRC OK -> accepted, data 10100001, send ACK 2
Sender  : frame 2 sent 01111000010
Receiver: frame 2 CRC OK -> accepted, data 01111000, send ACK 3
Sender  : frame 3 sent 11010010000   ~~ bit 1 CORRUPTED -> 10010010000
Receiver: frame 3 CRC remainder 100 != 0 -> ERROR detected, discarded
Sender  : cumulative ACK 3 received -> window slides from 0 to 3
Sender  : TIMEOUT for frame 3 -> Go-Back-N, resend from frame 3

=== Round 2 | window [3..6] ===
Sender  : frame 3 sent 11010010000
Receiver: frame 3 CRC OK -> accepted, data 11010010, send ACK 4
Sender  : frame 4 sent 00100010100   ~~ LOST in channel ~~
Sender  : frame 5 sent 11101011101
Receiver: frame 5 out of order (expected 4) -> discarded, re-ACK 4
Sender  : frame 6 sent 00001001110   ~~ bit 1 CORRUPTED -> 01001001110
Receiver: frame 6 CRC remainder 100 != 0 -> ERROR detected, discarded
Sender  : cumulative ACK 4 received -> window slides from 3 to 4
Sender  : TIMEOUT for frame 4 -> Go-Back-N, resend from frame 4

=== Round 3 | window [4..7] ===
Sender  : frame 4 sent 00100010100
Receiver: frame 4 CRC OK -> accepted, data 00100010, send ACK 5
Sender  : frame 5 sent 11101011101
Receiver: frame 5 CRC OK -> accepted, data 11101011, send ACK 6
Sender  : frame 6 sent 00001001110   ~~ bit 10 CORRUPTED -> 00001001111
Receiver: frame 6 CRC remainder 001 != 0 -> ERROR detected, discarded
Sender  : frame 7 sent 10110100111   ~~ LOST in channel ~~
Sender  : cumulative ACK 6 received -> window slides from 4 to 6
Sender  : TIMEOUT for frame 6 -> Go-Back-N, resend from frame 6

=== Round 4 | window [6..7] ===
Sender  : frame 6 sent 00001001110   ~~ LOST in channel ~~
Sender  : frame 7 sent 10110100111
Receiver: frame 7 out of order (expected 6) -> discarded, re-ACK 6
ACK 6 ~~ LOST in channel ~~
Sender  : TIMEOUT for frame 6 -> Go-Back-N, resend from frame 6

=== Round 5 | window [6..7] ===
Sender  : frame 6 sent 00001001110
Receiver: frame 6 CRC OK -> accepted, data 00001001, send ACK 7
Sender  : frame 7 sent 10110100111   ~~ LOST in channel ~~
Sender  : cumulative ACK 7 received -> window slides from 6 to 7
Sender  : TIMEOUT for frame 7 -> Go-Back-N, resend from frame 7

=== Round 6 | window [7..7] ===
Sender  : frame 7 sent 10110100111
Receiver: frame 7 CRC OK -> accepted, data 10110100, send ACK 8
Sender  : cumulative ACK 8 received -> window slides from 7 to 8

========== STATISTICS ==========
Frames delivered        : 8 (all, in order)
Total transmissions     : 17
Retransmissions         : 9
Frames lost in channel  : 4
Errors caught by CRC    : 3
Efficiency (useful/sent): 47.1 %
All frames transmitted successfully
```
With error rate 0: 8 transmissions, 0 retransmissions, **100% efficiency**.

## Explanation (answer to "Explain")
**Reliability**
| Mechanism | What it guarantees |
|---|---|
| **CRC** (modulo-2 division) | Detects corrupted frames. Every single-bit error, every burst ≤ 3 bits (degree of the generator), and all odd-numbered bit errors for generators like 1011 are caught. All 3 corruptions in the run were detected. |
| **Sequence numbers** | Receiver delivers frames **in order** and discards out-of-order or duplicate ones |
| **Cumulative ACKs** | One ACK confirms all earlier frames, so a lost ACK is covered by a later one |
| **Timeout + Go-Back-N** | Lost or corrupted frames are always resent, so all 8 frames were delivered despite 7 channel errors |

**Efficiency**
- **Pipelining:** up to W frames are in flight before waiting. Stop-and-wait sends 1 per round trip; a window of 4 gives up to 4× the throughput on long links.
- Utilisation = W / (1 + 2a), where a = propagation time ÷ transmission time. A bigger window keeps the link busy.
- Cost: Go-Back-N resends the whole window after an error (frame 7 was resent although only frame 6 was bad), which is why efficiency fell to 47% at 30% errors. **Selective Repeat** resends only the bad frames, at the cost of receiver buffering.
- CRC adds only 3 bits per 8-bit frame here (much less, relatively, for real 1500-byte frames with CRC-32).
- Window size must be ≤ 2^m − 1 for m-bit sequence numbers (Go-Back-N).

## Viva Questions
1. **Go-Back-N vs Selective Repeat?** GBN resends all frames from the lost one onward and the receiver keeps no buffer. SR resends only the lost frame and buffers out-of-order frames.
2. **Why must the GBN window be < 2^m?** So a new frame can't be confused with an old retransmitted one that has the same number.
3. **What does CRC detect?** All single-bit errors, all burst errors of length ≤ r (degree of the generator), and odd numbers of errors if (x+1) is a factor.
4. **Why append zeros before dividing?** It shifts the data left by r bits, making room for the remainder.
5. **What is piggybacking?** Sending ACKs inside data frames going the other way.
6. **What is a cumulative ACK?** "ACK n" means every frame before n has been received.

## Result
Go-Back-N sliding window with CRC error detection was implemented and simulated over an unreliable channel. All frames were delivered correctly and in order, CRC detected every corrupted frame, and the window gave pipelined, efficient transmission.
