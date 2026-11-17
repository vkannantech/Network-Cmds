import java.util.*;

// Q6 - LAN ARP simulation: request (broadcast) / reply (unicast), cache with aging,
//      static entries and ARP-spoofing detection.  Run: java ARPSimulator
public class ARPSimulator {
    static final String MY_IP = "192.168.1.10", MY_MAC = "AA:AA:AA:00:00:10";
    static final int TTL = 60;                                   // cache entry lifetime (seconds)

    // the LAN: IP -> real MAC of every host that is switched on
    static final Map<String, String> LAN = new LinkedHashMap<>();
    static final String ATTACKER_IP = "192.168.1.66", ATTACKER_MAC = "DE:AD:BE:EF:00:66";

    static class Entry {
        String mac; boolean isStatic; int created;
        Entry(String m, boolean s, int t) { mac = m; isStatic = s; created = t; }
    }
    static final Map<String, Entry> cache = new LinkedHashMap<>();
    static final Set<String> pending = new HashSet<>();          // IPs we have sent a request for
    static int clock = 0;                                        // simulated time in seconds
    static boolean security = true;

    public static void main(String[] args) {
        LAN.put("192.168.1.1", "00:1A:2B:3C:4D:01");   // gateway
        LAN.put("192.168.1.20", "00:1A:2B:3C:4D:20");  // PC2
        LAN.put("192.168.1.30", "00:1A:2B:3C:4D:30");  // PC3
        LAN.put("192.168.1.50", "00:1A:2B:3C:4D:50");  // File server
        LAN.put(ATTACKER_IP, ATTACKER_MAC);             // attacker machine
        cache.put("192.168.1.1", new Entry(LAN.get("192.168.1.1"), true, 0));  // gateway pinned as static

        Scanner sc = new Scanner(System.in);
        System.out.println("This host: " + MY_IP + " / " + MY_MAC);
        while (true) {
            System.out.println("\n===== ARP MENU (time = " + clock + "s, security " + (security ? "ON" : "OFF") + ") =====");
            System.out.println("1. Resolve IP (send ARP request)");
            System.out.println("2. Show ARP cache");
            System.out.println("3. Simulate ARP spoofing attack");
            System.out.println("4. Advance time (cache aging)");
            System.out.println("5. Toggle security ON/OFF");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            if (!sc.hasNextInt()) break;
            int ch = sc.nextInt();
            if (ch == 1) { System.out.print("Enter IP Address: "); resolve(sc.next()); }
            else if (ch == 2) showCache();
            else if (ch == 3) { System.out.print("IP the attacker wants to impersonate: "); spoof(sc.next()); }
            else if (ch == 4) { System.out.print("Seconds to advance: "); clock += sc.nextInt(); expire(); }
            else if (ch == 5) { security = !security; System.out.println("Security is now " + (security ? "ON" : "OFF")); }
            else if (ch == 6) { System.out.println("Program terminated"); break; }
            else System.out.println("Invalid choice");
        }
    }

    static void resolve(String ip) {
        expire();
        Entry e = cache.get(ip);
        if (e != null) {
            System.out.println("CACHE HIT : " + ip + " -> " + e.mac + (e.isStatic ? " (static)" : " (dynamic)") + "  - no packet sent");
            return;
        }
        System.out.println("CACHE MISS: sending ARP REQUEST (broadcast)");
        System.out.println("  [ARP REQUEST] src=" + MY_MAC + " dst=FF:FF:FF:FF:FF:FF : Who has " + ip + "? Tell " + MY_IP);
        pending.add(ip);
        for (String host : LAN.keySet()) {
            if (!host.equals(ip)) System.out.println("    " + host + " : not my IP, discard");
        }
        if (LAN.containsKey(ip)) {
            System.out.println("    " + ip + " : that's me! (also caches " + MY_IP + " -> " + MY_MAC + ")");
            receiveReply(ip, LAN.get(ip));
        } else {
            pending.remove(ip);
            System.out.println("  No ARP reply after 3 attempts -> Host " + ip + " unreachable");
        }
    }

    static void receiveReply(String ip, String mac) {
        System.out.println("  [ARP REPLY] (unicast) " + ip + " is at " + mac);
        Entry old = cache.get(ip);
        if (security) {
            if (!pending.contains(ip)) {
                System.out.println("  ** BLOCKED: unsolicited ARP reply (we never asked for " + ip + ") - ignored");
                return;
            }
            if (old != null && old.isStatic && !old.mac.equals(mac)) {
                System.out.println("  ** BLOCKED: " + ip + " is a STATIC entry (" + old.mac + "), reply with " + mac + " rejected");
                return;
            }
            if (old != null && !old.mac.equals(mac)) {
                System.out.println("  ** ALERT: MAC for " + ip + " changed " + old.mac + " -> " + mac + " - possible spoofing, ignored");
                return;
            }
        }
        pending.remove(ip);
        if (old != null && old.isStatic && !security) System.out.println("  (security OFF: static entry overwritten!)");
        cache.put(ip, new Entry(mac, false, clock));
        System.out.println("  Cache updated: " + ip + " -> " + mac + " (TTL " + TTL + "s)");
    }

    static void spoof(String victimIp) {
        System.out.println("Attacker " + ATTACKER_IP + " sends forged ARP REPLY: \"" + victimIp + " is at " + ATTACKER_MAC + "\"");
        receiveReply(victimIp, ATTACKER_MAC);
        Entry e = cache.get(victimIp);
        if (e != null && e.mac.equals(ATTACKER_MAC))
            System.out.println("  !!! CACHE POISONED: traffic for " + victimIp + " now goes to the attacker (man-in-the-middle)");
        else
            System.out.println("  Cache safe: attack defeated");
    }

    static void expire() {
        Iterator<Map.Entry<String, Entry>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Entry> m = it.next();
            if (!m.getValue().isStatic && clock - m.getValue().created >= TTL) {
                System.out.println("  Aging: entry " + m.getKey() + " expired and removed");
                it.remove();
            }
        }
    }

    static void showCache() {
        expire();
        System.out.println("Internet Address    Physical Address     Type      Age");
        for (Map.Entry<String, Entry> m : cache.entrySet()) {
            Entry e = m.getValue();
            System.out.printf("%-19s %-20s %-9s %s%n", m.getKey(), e.mac, e.isStatic ? "static" : "dynamic",
                    e.isStatic ? "-" : (clock - e.created) + "s");
        }
    }
}
