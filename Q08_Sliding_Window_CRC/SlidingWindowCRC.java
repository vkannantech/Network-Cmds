import java.util.*;

// Q8 - Go-Back-N sliding window + CRC error detection over a simulated unreliable channel.
// Inputs: number of frames, window size, channel error rate (%), generator polynomial (e.g. 1011)
public class SlidingWindowCRC {
    static Random rnd = new Random(42);           // fixed seed -> same run every time (change to vary)

    // modulo-2 division: returns the remainder (length = generator length - 1)
    static String mod2div(String dividend, String gen) {
        char[] r = dividend.toCharArray();
        for (int i = 0; i <= r.length - gen.length(); i++)
            if (r[i] == '1')
                for (int j = 0; j < gen.length(); j++)
                    r[i + j] = (r[i + j] == gen.charAt(j)) ? '0' : '1';
        return new String(r).substring(r.length - gen.length() + 1);
    }

    static String crcEncode(String data, String gen) {
        String zeros = new String(new char[gen.length() - 1]).replace('\0', '0');
        return data + mod2div(data + zeros, gen);              // codeword = data + CRC bits
    }

    static boolean crcCheck(String codeword, String gen) {
        return mod2div(codeword, gen).matches("0+");          // remainder 0 => no error detected
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of frames: ");          int n = sc.nextInt();
        System.out.print("Enter window size: ");               int w = sc.nextInt();
        System.out.print("Enter channel error rate (0-50 %): "); int err = sc.nextInt();
        System.out.print("Enter generator polynomial bits (e.g. 1011): "); String gen = sc.next();

        // build frames: 8 random data bits each, CRC appended
        String[] data = new String[n], frame = new String[n];
        for (int i = 0; i < n; i++) {
            StringBuilder b = new StringBuilder();
            for (int k = 0; k < 8; k++) b.append(rnd.nextInt(2));
            data[i] = b.toString();
            frame[i] = crcEncode(data[i], gen);
        }
        System.out.println("\n--- CRC encoding (generator " + gen + ") ---");
        for (int i = 0; i < n; i++)
            System.out.println("Frame " + i + ": data " + data[i] + "  CRC " + frame[i].substring(8) + "  -> transmitted " + frame[i]);

        int base = 0, expected = 0, round = 0, sent = 0, retrans = 0, crcErrors = 0, lost = 0;
        boolean[] everSent = new boolean[n];
        while (base < n) {
            round++;
            int end = Math.min(base + w, n);
            System.out.println("\n=== Round " + round + " | window [" + base + ".." + (end - 1) + "] ===");
            int lastAck = -1;
            for (int i = base; i < end; i++) {
                sent++;
                if (everSent[i]) retrans++;
                everSent[i] = true;
                String tx = frame[i];
                int r = rnd.nextInt(100);
                // ---- unreliable channel ----
                if (r < err / 2) {
                    lost++;
                    System.out.println("Sender  : frame " + i + " sent " + tx + "   ~~ LOST in channel ~~");
                    continue;
                }
                if (r < err) {                                    // flip one random bit
                    int pos = rnd.nextInt(tx.length());
                    char[] c = tx.toCharArray(); c[pos] = c[pos] == '0' ? '1' : '0';
                    System.out.println("Sender  : frame " + i + " sent " + tx + "   ~~ bit " + pos + " CORRUPTED -> " + new String(c));
                    tx = new String(c);
                } else {
                    System.out.println("Sender  : frame " + i + " sent " + tx);
                }
                // ---- receiver ----
                if (!crcCheck(tx, gen)) {
                    crcErrors++;
                    System.out.println("Receiver: frame " + i + " CRC remainder " + mod2div(tx, gen) + " != 0 -> ERROR detected, discarded");
                } else if (i != expected) {
                    System.out.println("Receiver: frame " + i + " out of order (expected " + expected + ") -> discarded, re-ACK " + expected);
                    lastAck = expected;
                } else {
                    expected++;
                    System.out.println("Receiver: frame " + i + " CRC OK -> accepted, data " + tx.substring(0, 8) + ", send ACK " + expected);
                    lastAck = expected;
                }
            }
            // cumulative ACK back to sender (ACK may also be lost)
            if (lastAck > 0 && rnd.nextInt(100) < err / 3) {
                System.out.println("ACK " + lastAck + " ~~ LOST in channel ~~");
                lastAck = -1;
            }
            if (lastAck > base) {
                System.out.println("Sender  : cumulative ACK " + lastAck + " received -> window slides from " + base + " to " + lastAck);
                base = lastAck;
            }
            if (base < end) System.out.println("Sender  : TIMEOUT for frame " + base + " -> Go-Back-N, resend from frame " + base);
        }

        System.out.println("\n========== STATISTICS ==========");
        System.out.println("Frames delivered        : " + n + " (all, in order)");
        System.out.println("Total transmissions     : " + sent);
        System.out.println("Retransmissions         : " + retrans);
        System.out.println("Frames lost in channel  : " + lost);
        System.out.println("Errors caught by CRC    : " + crcErrors);
        System.out.printf ("Efficiency (useful/sent): %.1f %%%n", 100.0 * n / sent);
        System.out.println("All frames transmitted successfully");
    }
}
