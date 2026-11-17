import java.net.*;
import java.util.*;

// Q5 - Custom DNS server over UDP (stateless: every request carries everything needed)
// Request : <id>|<domain or IP>
// Response: <id>|NOERROR|<answer>|<ttl>   or   <id>|NXDOMAIN|<query>   or   <id>|FORMERR|<reason>
public class DNSServer {
    static final int PORT = 5300;
    static final Map<String, String> A = new HashMap<>();     // domain -> IP   (forward lookup)
    static final Map<String, String> PTR = new HashMap<>();   // IP -> domain   (reverse lookup)

    static void add(String name, String ip) { A.put(name, ip); PTR.put(ip, name); }

    public static void main(String[] args) throws Exception {
        add("www.google.com", "142.250.183.4");
        add("www.aptitudeguru.com", "165.165.80.80");
        add("www.downloadcyclone.blogspot.com", "165.165.79.1");
        add("www.college.edu", "10.10.1.5");
        add("mail.college.edu", "10.10.1.6");
        add("lab.college.edu", "10.10.2.10");

        DatagramSocket ds = new DatagramSocket(PORT);
        System.out.println("DNS Server listening on UDP port " + PORT + " with " + A.size() + " records");
        int served = 0;
        while (true) {
            byte[] buf = new byte[512];                        // classic DNS UDP limit = 512 bytes
            DatagramPacket req = new DatagramPacket(buf, buf.length);
            ds.receive(req);
            long start = System.nanoTime();
            String msg = new String(req.getData(), 0, req.getLength()).trim();
            String reply = resolve(msg);
            byte[] out = reply.getBytes();
            ds.send(new DatagramPacket(out, out.length, req.getAddress(), req.getPort()));
            served++;
            System.out.printf("#%d %s:%d  query=\"%s\"  reply=\"%s\"  (%.3f ms)%n", served,
                    req.getAddress().getHostAddress(), req.getPort(), msg, reply, (System.nanoTime() - start) / 1e6);
        }
    }

    static String resolve(String msg) {
        String[] p = msg.split("\\|", 2);
        if (p.length != 2 || !p[0].matches("\\d+")) return "0|FORMERR|bad packet format";
        String id = p[0], q = p[1].trim().toLowerCase();
        if (q.isEmpty() || q.length() > 253) return id + "|FORMERR|empty or too long name";

        if (q.matches("\\d{1,3}(\\.\\d{1,3}){3}")) {                       // reverse lookup
            for (String part : q.split("\\.")) if (Integer.parseInt(part) > 255) return id + "|FORMERR|invalid IP address";
            return PTR.containsKey(q) ? id + "|NOERROR|" + PTR.get(q) + "|60" : id + "|NXDOMAIN|" + q;
        }
        if (!q.matches("[a-z0-9]([a-z0-9-]*[a-z0-9])?(\\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)+"))
            return id + "|FORMERR|invalid domain name";
        return A.containsKey(q) ? id + "|NOERROR|" + A.get(q) + "|60" : id + "|NXDOMAIN|" + q;
    }
}
