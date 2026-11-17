import java.net.*;
import java.util.*;

// Q5 - DNS resolver client: query IDs, timeout + retry, and a local TTL cache.
// Usage: java DNSClient [serverIP]      type a domain or IP, "cache" to view cache, "exit" to quit
public class DNSClient {
    static final int PORT = 5300, TIMEOUT_MS = 2000, RETRIES = 3;
    static final Map<String, String> cache = new HashMap<>();     // query -> answer
    static final Map<String, Long> expiry = new HashMap<>();      // query -> expiry time (ms)

    public static void main(String[] args) throws Exception {
        InetAddress server = InetAddress.getByName(args.length > 0 ? args[0] : "localhost");
        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(TIMEOUT_MS);
        Scanner sc = new Scanner(System.in);
        int nextId = new Random().nextInt(1000);

        while (true) {
            System.out.print("\nEnter DOMAIN NAME or IP (cache / exit): ");
            if (!sc.hasNextLine()) break;
            String q = sc.nextLine().trim().toLowerCase();
            if (q.isEmpty()) continue;
            if (q.equals("exit")) break;
            if (q.equals("cache")) {
                if (cache.isEmpty()) System.out.println("Cache is empty");
                for (String k : cache.keySet())
                    System.out.println("  " + k + " -> " + cache.get(k) + "  (expires in "
                            + (expiry.get(k) - System.currentTimeMillis()) / 1000 + " s)");
                continue;
            }

            long t0 = System.nanoTime();
            // 1. Check local cache first (performance improvement)
            if (cache.containsKey(q) && expiry.get(q) > System.currentTimeMillis()) {
                System.out.printf("Answer: %s   [from CACHE, %.3f ms]%n", cache.get(q), (System.nanoTime() - t0) / 1e6);
                continue;
            }

            // 2. Ask the server: UDP is unreliable/stateless, so use an ID + timeout + retry
            int id = ++nextId;
            byte[] out = (id + "|" + q).getBytes();
            String reply = null;
            for (int t = 1; t <= RETRIES && reply == null; t++) {
                ds.send(new DatagramPacket(out, out.length, server, PORT));
                try {
                    while (true) {
                        byte[] buf = new byte[512];
                        DatagramPacket p = new DatagramPacket(buf, buf.length);
                        ds.receive(p);
                        String r = new String(p.getData(), 0, p.getLength());
                        if (r.startsWith(id + "|")) { reply = r; break; }   // ignore stale replies of old queries
                    }
                } catch (SocketTimeoutException e) {
                    System.out.println("  timeout (attempt " + t + "/" + RETRIES + ")" + (t < RETRIES ? ", retrying..." : ""));
                }
            }
            if (reply == null) { System.out.println("Error: DNS server not responding"); continue; }

            String[] p = reply.split("\\|");
            double ms = (System.nanoTime() - t0) / 1e6;
            switch (p[1]) {
                case "NOERROR":
                    cache.put(q, p[2]);
                    expiry.put(q, System.currentTimeMillis() + Long.parseLong(p[3]) * 1000);
                    System.out.printf("Answer: %s   [from SERVER, id=%d, TTL=%ss, %.3f ms]%n", p[2], id, p[3], ms);
                    break;
                case "NXDOMAIN": System.out.println("Error NXDOMAIN: '" + q + "' does not exist"); break;
                default:         System.out.println("Error FORMERR: " + p[2]);
            }
        }
        ds.close();
        System.out.println("Resolver closed.");
    }
}
