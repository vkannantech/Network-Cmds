import java.net.*;
import java.util.*;

// Q7 - RARP server for diskless clients (over UDP), with optional DHCP-style dynamic pool.
// Usage: java RARPServer            -> hybrid mode (static RARP table first, then dynamic pool)
//        java RARPServer rarp-only  -> pure RARP (unknown MACs get no address)
// Request : RARP_REQUEST|<MAC>     Reply: RARP_REPLY|<MAC>|<IP>|<how>   or   RARP_NAK|<MAC>|<reason>
public class RARPServer {
    static final int PORT = 5400, LEASE_SEC = 120;
    static final Map<String, String> rarpTable = new LinkedHashMap<>();  // MAC -> IP (admin-configured)
    static final Map<String, String> leases = new LinkedHashMap<>();     // MAC -> IP (dynamic)
    static final Map<String, Long> leaseExpiry = new HashMap<>();
    static final Deque<String> pool = new ArrayDeque<>();

    public static void main(String[] args) throws Exception {
        boolean dynamic = !(args.length > 0 && args[0].equalsIgnoreCase("rarp-only"));
        rarpTable.put("AA:BB:CC:11:22:33", "192.168.1.11");
        rarpTable.put("AA:BB:CC:44:55:66", "192.168.1.12");
        rarpTable.put("AA:BB:CC:77:88:99", "192.168.1.13");
        for (int i = 100; i <= 102; i++) pool.add("192.168.1." + i);   // small pool to show exhaustion

        DatagramSocket ds = new DatagramSocket(PORT);
        System.out.println("RARP Server on UDP " + PORT + "  mode = " + (dynamic ? "HYBRID (RARP + DHCP-style pool)" : "RARP ONLY"));
        System.out.println("Static RARP table:");
        for (Map.Entry<String, String> e : rarpTable.entrySet()) System.out.println("   " + e.getKey() + " -> " + e.getValue());
        if (dynamic) System.out.println("Dynamic pool: " + pool + "  lease " + LEASE_SEC + "s");

        while (true) {
            byte[] buf = new byte[256];
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            ds.receive(p);
            String req = new String(p.getData(), 0, p.getLength()).trim();
            System.out.println("\nRequest from " + p.getAddress().getHostAddress() + ":" + p.getPort() + " : " + req);
            String reply = handle(req, dynamic);
            System.out.println("Reply : " + reply);
            byte[] out = reply.getBytes();
            ds.send(new DatagramPacket(out, out.length, p.getAddress(), p.getPort()));
        }
    }

    static String handle(String req, boolean dynamic) {
        String[] f = req.split("\\|");
        if (f.length != 2 || !f[0].equals("RARP_REQUEST")) return "RARP_NAK|?|malformed request";
        String mac = f[1].toUpperCase().replace('-', ':');
        if (!mac.matches("([0-9A-F]{2}:){5}[0-9A-F]{2}")) return "RARP_NAK|" + mac + "|invalid MAC format";

        if (rarpTable.containsKey(mac))                                   // 1. classic RARP lookup
            return "RARP_REPLY|" + mac + "|" + rarpTable.get(mac) + "|static RARP mapping";
        if (!dynamic)
            return "RARP_NAK|" + mac + "|no mapping in RARP table (admin must add it manually)";

        long now = System.currentTimeMillis();                             // 2. DHCP-style dynamic lease
        for (String m : new ArrayList<>(leases.keySet()))                  //    reclaim expired leases
            if (leaseExpiry.get(m) < now) { pool.add(leases.remove(m)); System.out.println("   lease of " + m + " expired, IP returned to pool"); }
        if (leases.containsKey(mac)) {
            leaseExpiry.put(mac, now + LEASE_SEC * 1000L);
            return "RARP_REPLY|" + mac + "|" + leases.get(mac) + "|dynamic lease RENEWED (" + LEASE_SEC + "s)";
        }
        if (pool.isEmpty()) return "RARP_NAK|" + mac + "|address pool exhausted";
        String ip = pool.poll();
        leases.put(mac, ip);
        leaseExpiry.put(mac, now + LEASE_SEC * 1000L);
        System.out.println("   allocated " + ip + " from pool, " + pool.size() + " left");
        return "RARP_REPLY|" + mac + "|" + ip + "|dynamic lease from pool (" + LEASE_SEC + "s)";
    }
}
