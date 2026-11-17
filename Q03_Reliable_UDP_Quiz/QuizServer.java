import java.net.*;
import java.util.*;

// Q3 - Real-time quiz over UDP with reliability added at application level:
//   sequence numbers + ACK + timeout/retransmission + duplicate detection.
// Usage: java QuizServer [lossProbability]     e.g. java QuizServer 0.3
// Messages:  Q|seq|question   ACK|seq   ANS|seq|answer   AACK|seq|result   END|score
public class QuizServer {
    static final int PORT = 7000, TIMEOUT_MS = 1000, MAX_RETRY = 60;   // keep re-sending up to 60 s
    static DatagramSocket ds;
    static InetAddress cAddr; static int cPort;
    static double loss; static Random rnd = new Random();
    static int retransmissions = 0, duplicates = 0;

    static String[][] QUIZ = {
        {"Which layer does TCP work in? (a) Network (b) Transport (c) Data Link", "b"},
        {"Port number of HTTP? (a) 21 (b) 25 (c) 80", "c"},
        {"UDP is (a) connection-oriented (b) connectionless", "b"},
        {"ARP maps (a) IP->MAC (b) MAC->IP (c) Name->IP", "a"}
    };

    // unreliable send: may be "lost" to simulate a bad network
    static void send(String msg) throws Exception {
        if (rnd.nextDouble() < loss) { System.out.println("   [SIMULATED LOSS] dropped: " + msg); return; }
        byte[] b = msg.getBytes();
        ds.send(new DatagramPacket(b, b.length, cAddr, cPort));
    }

    static String receive() throws Exception {
        byte[] buf = new byte[1024];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        ds.receive(p);                        // throws SocketTimeoutException after TIMEOUT_MS
        cAddr = p.getAddress(); cPort = p.getPort();
        return new String(p.getData(), 0, p.getLength());
    }

    public static void main(String[] args) throws Exception {
        loss = args.length > 0 ? Double.parseDouble(args[0]) : 0.0;
        ds = new DatagramSocket(PORT);
        System.out.println("Quiz Server on UDP " + PORT + " (simulated loss = " + (int) (loss * 100) + "%)");
        System.out.println("Waiting for a player to JOIN...");
        while (!receive().equals("JOIN")) { }
        System.out.println("Player joined from " + cAddr + ":" + cPort);
        ds.setSoTimeout(TIMEOUT_MS);

        int score = 0;
        Set<Integer> answered = new HashSet<>();
        for (int seq = 1; seq <= QUIZ.length; seq++) {
            String q = "Q|" + seq + "|" + QUIZ[seq - 1][0];
            // ---- stop-and-wait: send question until ACK for this seq arrives ----
            boolean acked = false;
            for (int t = 0; t <= MAX_RETRY && !acked; t++) {
                if (t > 0) { retransmissions++; System.out.println("   Timeout -> retransmit Q" + seq + " (try " + (t + 1) + ")"); }
                else System.out.println("Sending Q" + seq);
                send(q);
                long end = System.currentTimeMillis() + TIMEOUT_MS;
                while (System.currentTimeMillis() < end && !acked) {
                    try {
                        String m = receive();
                        if (m.equals("ACK|" + seq)) { acked = true; System.out.println("   ACK " + seq + " received"); }
                        else if (m.startsWith("ANS|")) {                 // an answer also acts as an ACK
                            score += handleAnswer(m, answered);
                            if (answered.contains(seq)) acked = true;
                        }
                    } catch (SocketTimeoutException e) { break; }
                }
            }
            if (!acked) { System.out.println("Player not responding - quiz aborted"); return; }

            // ---- wait (no time limit) for the player's answer to this question ----
            while (!answered.contains(seq)) {
                try {
                    String m = receive();
                    if (m.startsWith("ANS|")) score += handleAnswer(m, answered);
                    else if (m.equals("ACK|" + seq)) { duplicates++; System.out.println("   duplicate ACK " + seq + " ignored"); }
                } catch (SocketTimeoutException e) { /* player still thinking */ }
            }
        }
        for (int i = 0; i < 3; i++) send("END|" + score + "/" + QUIZ.length);   // send END a few times
        System.out.println("\nQuiz over. Score = " + score + "/" + QUIZ.length);
        System.out.println("Retransmissions = " + retransmissions + ", duplicates detected = " + duplicates);
        ds.close();
    }

    // returns 1 if a NEW correct answer, 0 otherwise. Duplicate answers are re-ACKed but not re-scored.
    static int handleAnswer(String m, Set<Integer> answered) throws Exception {
        String[] p = m.split("\\|", 3);
        int seq = Integer.parseInt(p[1]);
        String ans = p.length > 2 ? p[2].trim() : "";
        boolean correct = ans.equalsIgnoreCase(QUIZ[seq - 1][1]);
        if (answered.contains(seq)) {
            duplicates++;
            System.out.println("   DUPLICATE answer for Q" + seq + " detected -> not scored again, re-sending AACK");
            send("AACK|" + seq + "|" + (correct ? "Correct" : "Wrong"));
            return 0;
        }
        answered.add(seq);
        System.out.println("   Answer for Q" + seq + " = " + ans + " -> " + (correct ? "Correct" : "Wrong"));
        send("AACK|" + seq + "|" + (correct ? "Correct" : "Wrong, answer is " + QUIZ[seq - 1][1]));
        return correct ? 1 : 0;
    }
}
