import java.util.*;

// Q10 - Link State routing (OSPF style): Dijkstra SPF, link failure + LSA flooding,
//       and comparison with Distance Vector (convergence speed and accuracy).
// Input: n, n x n cost matrix (0 = no link), source router, then a link to fail.
public class LinkStateSim {
    static final int INF = Integer.MAX_VALUE / 2;
    static int n;
    static int[][] g;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of routers: ");
        n = sc.nextInt();
        g = new int[n][n];
        System.out.println("Enter cost matrix (0 = no link):");
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) g[i][j] = sc.nextInt();
        System.out.print("Enter source router (0 to " + (n - 1) + "): ");
        int src = sc.nextInt();

        System.out.println("\n========== BEFORE FAILURE ==========");
        int[] before = dijkstra(src, true);
        int[][] dvBefore = allPairsDV();                    // DV converged state before failure
        int[][] dvBeforeNext = new int[n][];
        for (int i = 0; i < n; i++) dvBeforeNext[i] = dvNext[i].clone();

        System.out.print("\nEnter link to FAIL (two routers, e.g. 2 4): ");
        int u = sc.nextInt(), v = sc.nextInt();
        if (g[u][v] == 0) { System.out.println("No such link!"); return; }
        g[u][v] = g[v][u] = 0;
        System.out.println("\n***** Link " + u + " - " + v + " FAILED *****");

        // ---- Link State reaction: LSA flooding then SPF ----
        int[] hopsU = bfs(u), hopsV = bfs(v);
        int floodRounds = 0, lsaMsgs = 0;
        for (int r = 0; r < n; r++) {
            int h = Math.min(hopsU[r], hopsV[r]);
            if (h < INF) floodRounds = Math.max(floodRounds, h);
        }
        for (int origin : new int[]{u, v}) {                 // each endpoint floods one LSA
            int[] hops = bfs(origin);
            for (int r = 0; r < n; r++)
                if (hops[r] < INF) lsaMsgs += degree(r) - (r == origin ? 0 : 1);
        }
        System.out.println("Routers " + u + " and " + v + " flood LSAs: \"link " + u + "-" + v + " is DOWN\"");
        System.out.println("LSAs reach every router in " + floodRounds + " hop(s), " + lsaMsgs + " LSA messages; every router re-runs SPF");

        System.out.println("\n========== AFTER FAILURE (new SPF) ==========");
        int[] after = dijkstra(src, true);
        System.out.println("\nChanged routes from router " + src + ":");
        boolean any = false;
        for (int d = 0; d < n; d++)
            if (before[d] != after[d]) { any = true;
                System.out.println("  to " + d + ": " + show(before[d]) + " -> " + show(after[d])); }
        if (!any) System.out.println("  none");

        // ---- Distance Vector on the same failure ----
        int[] dv = dvAfterFailure(dvBefore, dvBeforeNext, u, v);           // {rounds, messages, wrongEntriesSeen}
        int lsRounds = floodRounds + 1;                       // flooding + one SPF run

        int[][] dvFinal = allPairsDV();
        boolean same = true;
        for (int s = 0; s < n; s++) { int[] ls = dijkstra(s, false); for (int d = 0; d < n; d++) if (ls[d] != dvFinal[s][d]) same = false; }

        System.out.println("\n================ LINK STATE vs DISTANCE VECTOR ================");
        System.out.printf("%-38s %-14s %-14s%n", "Metric", "Link State", "Distance Vec");
        System.out.printf("%-38s %-14d %-14d%n", "Convergence (rounds)", lsRounds, dv[0]);
        System.out.printf("%-38s %-14d %-14d%n", "Control messages after failure", lsaMsgs, dv[1]);
        System.out.printf("%-38s %-14s %-14d%n", "Wrong route entries while converging", "0", dv[2]);
        System.out.printf("%-38s %-14s %-14s%n", "Final shortest paths", "optimal", same ? "same (optimal)" : "DIFFERENT");
        System.out.printf("%-38s %-14s %-14s%n", "Knowledge of topology", "full map", "neighbours only");
        System.out.println("\nConclusion: Link State converges faster and has no transient loops because every");
        System.out.println("router computes paths from a complete map; DV needs several exchange rounds.");
    }

    static String show(int c) { return c >= INF ? "unreachable" : String.valueOf(c); }

    static int degree(int r) { int d = 0; for (int j = 0; j < n; j++) if (g[r][j] != 0) d++; return d; }

    static int[] bfs(int s) {                               // hop count from s
        int[] h = new int[n]; Arrays.fill(h, INF); h[s] = 0;
        Deque<Integer> q = new ArrayDeque<>(); q.add(s);
        while (!q.isEmpty()) { int x = q.poll(); for (int y = 0; y < n; y++) if (g[x][y] != 0 && h[y] == INF) { h[y] = h[x] + 1; q.add(y); } }
        return h;
    }

    // Dijkstra's SPF. verbose = print step table + routing table with paths
    static int[] dijkstra(int src, boolean verbose) {
        int[] dist = new int[n], prev = new int[n];
        boolean[] done = new boolean[n];
        Arrays.fill(dist, INF); Arrays.fill(prev, -1);
        dist[src] = 0;
        if (verbose) System.out.println("Step  Selected  Distances " + Arrays.toString(range()));
        for (int step = 1; step <= n; step++) {
            int u = -1;
            for (int i = 0; i < n; i++) if (!done[i] && dist[i] < INF && (u == -1 || dist[i] < dist[u])) u = i;
            if (u == -1) break;
            done[u] = true;
            for (int v = 0; v < n; v++)
                if (g[u][v] != 0 && !done[v] && dist[u] + g[u][v] < dist[v]) { dist[v] = dist[u] + g[u][v]; prev[v] = u; }
            if (verbose) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < n; i++) sb.append(String.format("%5s", dist[i] >= INF ? "inf" : dist[i]));
                System.out.printf("%-5d %-9d %s%n", step, u, sb);
            }
        }
        if (verbose) {
            System.out.println("\nRouting table of router " + src + ":");
            System.out.println("Dest  Cost   NextHop  Path");
            for (int d = 0; d < n; d++) {
                if (dist[d] >= INF) { System.out.printf("%-5d %-6s %-8s %s%n", d, "INF", "-", "unreachable"); continue; }
                LinkedList<Integer> path = new LinkedList<>();
                for (int x = d; x != -1; x = prev[x]) path.addFirst(x);
                String nh = path.size() > 1 ? String.valueOf(path.get(1)) : "-";
                StringBuilder ps = new StringBuilder();
                for (int x : path) ps.append(ps.length() > 0 ? " -> " : "").append(x);
                System.out.printf("%-5d %-6d %-8s %s%n", d, dist[d], nh, ps);
            }
        }
        return dist;
    }

    static int[] range() { int[] r = new int[n]; for (int i = 0; i < n; i++) r[i] = i; return r; }

    // ---------- Distance Vector (synchronous Bellman-Ford with poison reverse) ----------
    static int[][] dvDist, dvNext;

    static int[][] allPairsDV() {
        dvDist = new int[n][n]; dvNext = new int[n][n];
        for (int i = 0; i < n; i++) for (int d = 0; d < n; d++) {
            dvDist[i][d] = i == d ? 0 : g[i][d] != 0 ? g[i][d] : INF;
            dvNext[i][d] = i == d ? i : g[i][d] != 0 ? d : -1;
        }
        dvRounds(null);
        int[][] r = new int[n][]; for (int i = 0; i < n; i++) r[i] = dvDist[i].clone();
        return r;
    }

    static int[] dvAfterFailure(int[][] beforeDist, int[][] beforeNext, int u, int v) {
        dvDist = new int[n][]; dvNext = new int[n][];                // start from converged old tables
        for (int i = 0; i < n; i++) { dvDist[i] = beforeDist[i].clone(); dvNext[i] = beforeNext[i].clone(); }
        for (int d = 0; d < n; d++) {                                 // endpoints detect the dead link
            if (dvNext[u][d] == v) { dvDist[u][d] = INF; dvNext[u][d] = -1; }
            if (dvNext[v][d] == u) { dvDist[v][d] = INF; dvNext[v][d] = -1; }
        }
        int[][] correct = new int[n][];                               // true answer, to count wrong entries
        for (int s = 0; s < n; s++) correct[s] = dijkstra(s, false);
        return dvRounds(correct);
    }

    static int[] dvRounds(int[][] correct) {
        int rounds = 0, msgs = 0, wrong = 0;
        while (rounds < 100) {
            int[][] old = new int[n][], oldNext = new int[n][];
            for (int i = 0; i < n; i++) { old[i] = dvDist[i].clone(); oldNext[i] = dvNext[i].clone(); }
            boolean changed = false;
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
                if (i == j || g[i][j] == 0) continue;
                msgs++;
                for (int d = 0; d < n; d++) {
                    if (d == i) continue;
                    int adv = oldNext[j][d] == i ? INF : old[j][d];          // poison reverse
                    int nc = adv >= INF ? INF : Math.min(INF, g[i][j] + adv);
                    if ((dvNext[i][d] == j && nc != dvDist[i][d]) || nc < dvDist[i][d]) {
                        dvDist[i][d] = nc; dvNext[i][d] = nc >= INF ? -1 : j; changed = true;
                    }
                }
            }
            if (!changed) break;
            rounds++;
            if (correct != null)
                for (int i = 0; i < n; i++) for (int d = 0; d < n; d++) if (dvDist[i][d] != correct[i][d]) wrong++;
        }
        return new int[]{rounds, msgs, wrong};
    }
}
