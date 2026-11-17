import java.util.*;

// Q11 - Discrete-time simulation of an enterprise network running RIP (distance vector)
//       and OSPF (link state). Scenarios: link failure, traffic congestion, topology change.
//       Metrics: convergence time, packet delivery ratio (PDR), routing overhead.
// Run: java RoutingSimulator
public class RoutingSimulator {
    static final int N = 8, SIM_TIME = 120, EVENT_T = 10, INF = 16;
    static final int RIP_PERIOD = 30, HELLO = 10, SPF_DELAY = 2, PKTS_PER_SEC = 10;
    static String nm(int r) { return "R" + (r + 1); }

    // ---------------- topology ----------------
    static double[][] bw;          // Mbps, 0 = no link
    static double[][] cap;         // packets per second the link can carry
    static double[][] bg;          // background traffic (pkt/s) on a link

    static void addLink(int a, int b, double mbps, double pps) { bw[a][b] = bw[b][a] = mbps; cap[a][b] = cap[b][a] = pps; }

    static void baseTopology() {
        bw = new double[N][N]; cap = new double[N][N]; bg = new double[N][N];
        // two 1 Gbps core paths between R1 and R8, a cross-link, and one slow 2 Mbps WAN shortcut
        addLink(0, 1, 1000, 1000); addLink(1, 2, 1000, 1000); addLink(2, 3, 1000, 1000); addLink(3, 7, 1000, 1000);
        addLink(0, 4, 1000, 1000); addLink(4, 5, 1000, 1000); addLink(5, 6, 1000, 1000); addLink(6, 7, 1000, 1000);
        addLink(1, 5, 1000, 1000);
        addLink(0, 7, 2, 25);      // WAN: few hops but low bandwidth
    }

    static final int[][] FLOWS = {{0, 7}, {7, 0}, {1, 6}, {4, 3}, {2, 4}, {5, 3}};

    // ---------------- routing algorithms ----------------
    static int ospfCost(int a, int b) { return (int) Math.max(1, 1000 / bw[a][b]); }  // reference bw 1000 Mbps

    static int[][] ospfTables() {                       // Dijkstra from every router -> next hop
        int[][] next = new int[N][N];
        for (int s = 0; s < N; s++) {
            int[] dist = new int[N], first = new int[N]; boolean[] done = new boolean[N];
            Arrays.fill(dist, Integer.MAX_VALUE); Arrays.fill(first, -1); dist[s] = 0;
            for (int k = 0; k < N; k++) {
                int u = -1;
                for (int i = 0; i < N; i++) if (!done[i] && dist[i] != Integer.MAX_VALUE && (u == -1 || dist[i] < dist[u])) u = i;
                if (u == -1) break;
                done[u] = true;
                for (int v = 0; v < N; v++)
                    if (bw[u][v] > 0 && dist[u] + ospfCost(u, v) < dist[v]) {
                        dist[v] = dist[u] + ospfCost(u, v); first[v] = (u == s) ? v : first[u];
                    }
            }
            next[s] = first;
        }
        return next;
    }

    static int[][] ripDist, ripNext;

    static void ripConverge() {                          // start RIP fully converged (hop count)
        ripDist = new int[N][N]; ripNext = new int[N][N];
        for (int i = 0; i < N; i++) for (int d = 0; d < N; d++) {
            ripDist[i][d] = i == d ? 0 : bw[i][d] > 0 ? 1 : INF; ripNext[i][d] = i == d ? i : bw[i][d] > 0 ? d : -1;
        }
        for (int it = 0; it < N; it++)
            for (int i = 0; i < N; i++) for (int j = 0; j < N; j++) if (bw[i][j] > 0)
                for (int d = 0; d < N; d++) if (1 + ripDist[j][d] < ripDist[i][d]) { ripDist[i][d] = 1 + ripDist[j][d]; ripNext[i][d] = j; }
    }

    // ---------------- forwarding + congestion ----------------
    static List<int[]> path(int[][] next, int s, int d) {   // list of links, null if dropped
        List<int[]> links = new ArrayList<>();
        int cur = s, ttl = 16;
        while (cur != d) {
            int nh = next[cur][d];
            if (nh < 0 || bw[cur][nh] == 0 || --ttl == 0) return null;   // no route / dead link / loop
            links.add(new int[]{cur, nh});
            cur = nh;
        }
        return links;
    }

    static double[] deliver(int[][] next) {              // returns {sent, delivered} for one second
        List<List<int[]>> paths = new ArrayList<>();
        double[][] load = new double[N][N];
        for (int i = 0; i < N; i++) for (int j = 0; j < N; j++) load[i][j] = bg[i][j];
        for (int[] f : FLOWS) {
            List<int[]> p = path(next, f[0], f[1]);
            paths.add(p);
            if (p != null) for (int[] l : p) load[l[0]][l[1]] += PKTS_PER_SEC;
        }
        double sent = 0, ok = 0;
        for (List<int[]> p : paths) {
            sent += PKTS_PER_SEC;
            if (p == null) continue;
            double frac = 1;
            for (int[] l : p) if (load[l[0]][l[1]] > cap[l[0]][l[1]]) frac *= cap[l[0]][l[1]] / load[l[0]][l[1]];
            ok += PKTS_PER_SEC * frac;
        }
        return new double[]{sent, ok};
    }

    static String route(int[][] next, int s, int d) {
        List<int[]> p = path(next, s, d);
        if (p == null) return "NO ROUTE";
        StringBuilder sb = new StringBuilder(nm(s));
        for (int[] l : p) sb.append("-").append(nm(l[1]));
        return sb.toString();
    }

    // ---------------- one scenario for one protocol ----------------
    static int degree(int r) { int c = 0; for (int j = 0; j < N; j++) if (bw[r][j] > 0) c++; return c; }

    static double[] run(String proto, int scenario, boolean print) {
        baseTopology();
        ripConverge();
        int[][] ospfOld = ospfTables(), ospfNew = null;
        int[] ospfReady = new int[N];
        double sent = 0, ok = 0, msgs = 0, bytes = 0;
        int lastChange = EVENT_T;
        int[] phase = new int[N];
        for (int r = 0; r < N; r++) phase[r] = (r * 7) % RIP_PERIOD;   // RIP timers are not synchronised
        int[] fl = FLOWS[scenario == 1 ? 2 : scenario == 3 ? 4 : 0];
        String before = "";

        for (int t = 0; t < SIM_TIME; t++) {
            if (t == EVENT_T) {
                before = route(proto.equals("RIP") ? ripNext : ospfOld, fl[0], fl[1]);
                int a = 0, b = 0;
                if (scenario == 1) { a = 5; b = 6; bw[a][b] = bw[b][a] = 0; }                  // link R6-R7 fails
                if (scenario == 2) { bg[0][7] = 15; }                                           // backup job floods WAN
                if (scenario == 3) { a = 3; b = 4; addLink(a, b, 1000, 1000); }                // new link R4-R5
                if (proto.equals("RIP") && scenario != 2) {
                    for (int d = 0; d < N; d++) {
                        if (scenario == 1) {                       // attached routers invalidate routes
                            if (ripNext[a][d] == b) { ripDist[a][d] = INF; ripNext[a][d] = -1; }
                            if (ripNext[b][d] == a) { ripDist[b][d] = INF; ripNext[b][d] = -1; }
                        }
                    }
                    if (scenario == 3) { ripDist[a][b] = ripDist[b][a] = 1; ripNext[a][b] = b; ripNext[b][a] = a; }
                }
                if (proto.equals("OSPF") && scenario != 2) {   // LSA flooding from both endpoints
                    ospfNew = ospfTables();
                    int[] hop = new int[N]; Arrays.fill(hop, 99); hop[a] = hop[b] = 0;
                    Deque<Integer> q = new ArrayDeque<>(Arrays.asList(a, b));
                    while (!q.isEmpty()) { int x = q.poll(); for (int y = 0; y < N; y++) if (bw[x][y] > 0 && hop[y] == 99) { hop[y] = hop[x] + 1; q.add(y); } }
                    for (int r = 0; r < N; r++) {
                        ospfReady[r] = t + hop[r] + SPF_DELAY;        // 1 s per flooding hop + SPF delay
                        lastChange = Math.max(lastChange, ospfReady[r]);
                        msgs += 2 * Math.max(1, degree(r) - 1); bytes += 2 * Math.max(1, degree(r) - 1) * 100;  // 2 LSAs
                    }
                }
            }

            int[][] next;
            if (proto.equals("RIP")) {
                for (int r = 0; r < N; r++) {
                    if ((t + phase[r]) % RIP_PERIOD != 0) continue;
                    for (int j = 0; j < N; j++) {
                        if (bw[r][j] == 0) continue;
                        msgs++; bytes += 32 + 20 * N;            // RIP header + 20 bytes per route
                        for (int d = 0; d < N; d++) {
                            if (d == j) continue;
                            int adv = ripNext[r][d] == j ? INF : ripDist[r][d];     // poison reverse
                            int nc = Math.min(INF, 1 + adv);
                            if ((ripNext[j][d] == r && nc != ripDist[j][d]) || nc < ripDist[j][d]) {
                                ripDist[j][d] = nc; ripNext[j][d] = nc >= INF ? -1 : r;
                                if (t >= EVENT_T) lastChange = t;
                            }
                        }
                    }
                }
                next = ripNext;
            } else {
                if (t % HELLO == 0) for (int r = 0; r < N; r++) { msgs += degree(r); bytes += degree(r) * 64; }
                next = new int[N][];
                for (int r = 0; r < N; r++) next[r] = (ospfNew != null && t >= ospfReady[r]) ? ospfNew[r] : ospfOld[r];
            }

            double[] res = deliver(next);
            sent += res[0]; ok += res[1];
            if (print && t == SIM_TIME - 1)
                System.out.printf("   %-5s flow %s->%s : before %-22s after %s%n", proto, nm(fl[0]), nm(fl[1]), before, route(next, fl[0], fl[1]));
        }
        int conv = scenario == 2 ? 0 : lastChange - EVENT_T;
        return new double[]{conv, 100.0 * ok / sent, msgs, bytes};
    }

    public static void main(String[] args) {
        String[] names = {"", "Link failure (R6-R7 down)", "Traffic congestion on WAN", "Topology change (new R4-R5)"};
        System.out.println("Enterprise network: 8 routers, 1 Gbps core links + one 2 Mbps WAN link R1-R8");
        System.out.println("Traffic: " + FLOWS.length + " flows x " + PKTS_PER_SEC + " pkt/s, simulated " + SIM_TIME + " s, event at t=" + EVENT_T + " s");
        System.out.println("RIP: hop-count metric, updates every " + RIP_PERIOD + " s, poison reverse");
        System.out.println("OSPF: cost = 1000/bandwidth, hello every " + HELLO + " s, LSA flooding + SPF (" + SPF_DELAY + " s delay)");

        double[][][] r = new double[4][2][];
        for (int s = 1; s <= 3; s++) {
            System.out.println("\n=== Scenario " + s + ": " + names[s] + " ===");
            r[s][0] = run("RIP", s, true);
            r[s][1] = run("OSPF", s, true);
        }
        System.out.println("\n============================== RESULTS ==============================");
        System.out.printf("%-30s %-6s %-12s %-9s %-9s %-10s%n", "Scenario", "Proto", "Converge(s)", "PDR(%)", "Messages", "Bytes");
        for (int s = 1; s <= 3; s++)
            for (int p = 0; p < 2; p++)
                System.out.printf("%-30s %-6s %-12s %-9.2f %-9.0f %-10.0f%n", p == 0 ? names[s] : "", p == 0 ? "RIP" : "OSPF",
                        s == 2 ? "n/a" : String.valueOf((int) r[s][p][0]), r[s][p][1], r[s][p][2], r[s][p][3]);

        double ripPdr = 0, ospfPdr = 0, ripConv = 0, ospfConv = 0;
        for (int s = 1; s <= 3; s++) { ripPdr += r[s][0][1]; ospfPdr += r[s][1][1]; if (s != 2) { ripConv += r[s][0][0]; ospfConv += r[s][1][0]; } }
        System.out.printf("%nAverage PDR : RIP %.2f %%   OSPF %.2f %%%n", ripPdr / 3, ospfPdr / 3);
        System.out.printf("Average convergence : RIP %.0f s   OSPF %.0f s%n", ripConv / 2, ospfConv / 2);
        System.out.println("\nScalability (steady-state overhead, bytes per minute, avg 3 links per router):");
        System.out.printf("%-10s %-14s %-14s %s%n", "Routers", "RIP", "OSPF", "RIP / OSPF");
        for (int n : new int[]{8, 16, 32, 64, 128}) {
            double links = 1.5 * n;
            double rip = (60.0 / RIP_PERIOD) * 2 * links * (32 + 20 * n);   // full table every 30 s
            double ospf = (60.0 / HELLO) * 2 * links * 64;                    // small hellos only
            System.out.printf("%-10d %-14.0f %-14.0f %.1fx%n", n, rip, ospf, rip / ospf);
        }
        System.out.println("(RIP also cannot use paths longer than 15 hops; OSPF scales further with areas.)");
        System.out.println("Most efficient protocol for a scalable enterprise network: "
                + (ospfPdr >= ripPdr && ospfConv <= ripConv ? "OSPF (link state)" : "RIP (distance vector)"));
    }
}
