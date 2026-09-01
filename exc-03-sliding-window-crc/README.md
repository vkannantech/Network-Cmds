# Exercise 3: Implementation of Sliding Window and CRC Protocols

## 1. Experiment Number & Title
- **Experiment No**: 3
- **Manual Title**: Implementation of sliding window and CRC protocols.
- **Course**: Networks-Commands-Deeply Engineering Suite

---

## 2. Aim
To write Java programs for simulating the **Sliding Window Protocol** for reliable flow-controlled transmission and implementing **Cyclic Redundancy Check (CRC)** error-detecting code with modulo-2 binary division.

---

## 3. Requirements & Software
- **Operating System**: Windows 10 / Windows 11
- **Software**: Java Development Kit (JDK 8 or higher)
- **Editor / Shell**: Command Prompt or PowerShell

---

## 4. Concepts Covered
- **Flow Control & Pipelining**: Stop-and-Wait vs Sliding Window (Go-Back-N & Selective Repeat), cumulative ACKs.
- **CRC Error Detection**: Generator polynomial $G(x)$, appending $m-1$ zero bits, modulo-2 XOR division without carry.

---

## 5. Sample Input & Mathematical Verification

### Sliding Window
- Frames: `8`, Window: `4`
- Outputs: Cumulative ACKs at frame 4 and frame 8.

### CRC Modulo-2 Division
- Data: `1 0 0 1 1 0 1` (7 bits)
- Generator: `1 0 1 1` (4 bits -> 3 zeros appended: `1001101000`)
- **Computed Remainder (CRC bits)**: `101`
- **Transmitted Codeword**: `1001101101`
- **Manual Note**: The manual printed sample lists `100`/`1001101100`. Standard binary XOR division mathematically yields `101`/`1001101101`.

---

## 6. How to Run

### Sliding Window:
- CMD: `run_sliding_window.bat`
- PowerShell: `.
un_sliding_window.ps1`

### CRC:
- CMD: `run_crc.bat`
- PowerShell: `.
un_crc.ps1`

---

## 7. Viva Questions & Answers (14 Questions)
1. **Advantage of Sliding Window over Stop-and-Wait?** Allows transmitting multiple frames before ACK, maximizing link throughput.
2. **What is window size?** Maximum unacknowledged frames sender can transmit into pipeline.
3. **Go-Back-N vs Selective Repeat?** GBN retransmits all frames from lost frame; SR retransmits only lost frame using receiver buffers.
4. **What layer does CRC operate on?** Data Link Layer (Layer 2 FCS) and Transport Layer.
5. **Why XOR in CRC?** Binary modulo-2 arithmetic without carry/borrow equals bitwise XOR.
6. **How many zero bits are appended?** $m - 1$ zeros (degree of generator polynomial).
7. **How does receiver verify CRC?** Divides received codeword by $G(x)$; zero remainder indicates error-free transmission.
8. **What errors does CRC detect?** All single-bit, double-bit, odd-numbered bit errors, and burst errors $\le m-1$.
9. **Standard Ethernet CRC?** CRC-32 (IEEE 802.3).
10. **What is cumulative ACK?** Single ACK acknowledging all frames up to sequence number $N$.
11. **What is piggybacking?** Attaching ACKs to outbound data frames traveling in reverse direction.
12. **Why must generator MSB be 1?** To align properly with the leading non-zero dividend bit during division.
13. **Max window size in GBN?** $2^k - 1$.
14. **What is Bandwidth-Delay Product (BDP)?** Bandwidth $	imes$ RTT; defines optimal window size.

---

## 8. Result Statement
Thus, the Java programs for implementing Sliding Window Protocol and CRC Protocol were executed and verified successfully.
