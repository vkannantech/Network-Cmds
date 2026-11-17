import java.net.*;
import java.util.*;

// Q3 - Quiz player (UDP). Usage: java QuizClient [serverIP] [lossProbability]
public class QuizClient {
    static final int PORT = 7000, TIMEOUT_MS = 1000, MAX_RETRY = 5;
    static DatagramSocket ds; static InetAddress server;
    static double loss; static Random rnd = new Random();

    static void send(String msg) throws Exception {
        if (rnd.nextDouble() < loss) { System.out.println("   [SIMULATED LOSS] dropped: " + msg); return; }
        byte[] b = msg.getBytes();
        ds.send(new DatagramPacket(b, b.length, server, PORT));
    }

    static String receive() throws Exception {
        byte[] buf = new byte[1024];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        ds.receive(p);
        return new String(p.getData(), 0, p.getLength());
    }

    public static void main(String[] args) throws Exception {
        server = InetAddress.getByName(args.length > 0 ? args[0] : "localhost");
        loss = args.length > 1 ? Double.parseDouble(args[1]) : 0.0;
        ds = new DatagramSocket();
        ds.setSoTimeout(TIMEOUT_MS);
        Scanner sc = new Scanner(System.in);
        int lastSeq = 0;                                  // highest question already shown

        String msg = null;
        for (int t = 0; t < 10 && msg == null; t++) {     // JOIN until the first question arrives
            send("JOIN");
            try { msg = receive(); } catch (SocketTimeoutException e) { System.out.println("   no reply, re-sending JOIN"); }
        }
        if (msg == null) { System.out.println("Server not reachable"); return; }

        while (true) {
            if (msg == null) {
                try { msg = receive(); } catch (SocketTimeoutException e) { continue; }
            }
            String[] p = msg.split("\\|", 3);
            msg = null;
            if (p[0].equals("END")) { System.out.println("\n*** Quiz finished. Your score: " + p[1] + " ***"); break; }
            if (!p[0].equals("Q")) continue;                         // stale AACK etc.

            int seq = Integer.parseInt(p[1]);
            send("ACK|" + seq);                                      // always ACK (the old ACK may be lost)
            if (seq <= lastSeq) { System.out.println("   DUPLICATE Q" + seq + " discarded (ACK re-sent)"); continue; }
            lastSeq = seq;

            System.out.println("\nQ" + seq + ". " + p[2]);
            System.out.print("Your answer: ");
            String ans = sc.hasNextLine() ? sc.nextLine().trim() : "";

            // send answer reliably: retransmit until AACK|seq arrives
            boolean done = false;
            for (int t = 0; t <= MAX_RETRY && !done; t++) {
                if (t > 0) System.out.println("   Timeout -> retransmit answer " + seq);
                send("ANS|" + seq + "|" + ans);
                try {
                    while (true) {
                        String m = receive();
                        if (m.startsWith("AACK|" + seq + "|")) { System.out.println("   Server: " + m.split("\\|", 3)[2]); done = true; break; }
                        if (m.startsWith("Q|" + seq + "|")) { send("ACK|" + seq); System.out.println("   DUPLICATE Q" + seq + " discarded"); }
                        else if (m.startsWith("Q|") || m.startsWith("END")) {     // AACK lost, but server moved on
                            System.out.println("   (result lost, but server moved on => answer " + seq + " was received)");
                            msg = m; done = true; break;
                        }
                    }
                } catch (SocketTimeoutException e) { }
            }
        }
        ds.close();
    }
}
