import java.util.*;

// Q9 - Distance Vector routing (RIP style) with route updates, convergence,
//      link failure, and loop prevention: NONE vs SPLIT HORIZON vs POISON REVERSE.
// Input: n, n x n cost matrix (999 = no link), then a link to fail (two router numbers).
public class DistanceVectorSim {
    static final int INF = 16;                       // RIP "infinity"
    static final String[] MODE = {"No Split Horizon", "Split Horizon", "Poison Reverse"};
    static int n;
    static int[][] cost;                             // current link costs (INF = no link)

    static class State {
        int[][] dist, next;
        State(int n) { dist = new int[n][n]; next = new int[n][n]; }
        State copy() { State s = new State(n); for (int i = 0; i < n; i++) { s.dist[i] = dist[i].clone(); s.next[i] = next[i].clone(); } return s; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of routers: ");
        n = sc.nextInt();
        cost = new int[n][n];
        System.out.println("Enter the cost matrix (999 = no link):");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) { int c = sc.nextInt(); cost[i][j] = (c >= 999 && i != j) ? INF : c; }

        // ---------- Phase 1: initial convergence ----------
        State s = new State(n);
        for (int i = 0; i < n; i++)
            for (int d = 0; d < n; d++) { s.dist[i][d] = cost[i][d]; s.next[i][d] = cost[i][d] < INF ? d : -1; }
        System.out.println("\n===== PHASE 1: Initial route updates (Poison Reverse) =====");
        int[] r1 = run(s, 2, true);
        System.out.println("Converged after " + r1[0] + " rounds, " + r1[1] + " update messages");
        print(s);

        // ---------- Phase 2: link failure ----------
        System.out.print("\nEnter link to FAIL (two router numbers, e.g. 3 5): ");
        int u = sc.nextInt() - 1, v = sc.nextInt() - 1;
        cost[u][v] = cost[v][u] = INF;
        System.out.println("\n***** Link R" + (u + 1) + " - R" + (v + 1) + " FAILED *****");

        int[][] result = new int[3][];
        State finalPR = null;
        for (int m = 0; m < 3; m++) {
            State t = s.copy();
            for (int d = 0; d < n; d++) {               // directly attached routers detect the failure
                if (t.next[u][d] == v) { t.dist[u][d] = INF; t.next[u][d] = -1; }
                if (t.next[v][d] == u) { t.dist[v][d] = INF; t.next[v][d] = -1; }
            }
            System.out.println("\n===== PHASE 2 with " + MODE[m] + " =====");
            result[m] = run(t, m, true);
            if (m == 2) finalPR = t;
        }
        System.out.println("\nFinal routing tables after failure (Poison Reverse):");
        print(finalPR);

        // ---------- Performance evaluation ----------
        System.out.println("\n================ PERFORMANCE COMPARISON (after failure) ================");
        System.out.printf("%-18s %-8s %-10s %s%n", "Technique", "Rounds", "Messages", "Count-to-infinity?");
        for (int m = 0; m < 3; m++)
            System.out.printf("%-18s %-8d %-10d %s%n", MODE[m], result[m][0], result[m][1], result[m][2] == 1 ? "YES - costs keep climbing" : "No - only short transient");
    }

    // Runs synchronous DV rounds until nothing changes. Returns {rounds, messages, countedToInfinity}
    static int[] run(State s, int mode, boolean verbose) {
        int rounds = 0, msgs = 0; boolean cti = false;
        int[][] bad = new int[n][n];                      // times a route was re-learned with a finite cost
        while (rounds < 100) {
            State old = s.copy();                         // every router sends its vector of the previous round
            boolean changed = false;
            StringBuilder log = new StringBuilder();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {            // router i receives vector from neighbour j
                    if (i == j || cost[i][j] >= INF) continue;
                    msgs++;
                    for (int d = 0; d < n; d++) {
                        if (d == i) continue;
                        int adv = old.dist[j][d];
                        if (old.next[j][d] == i) {               // j's route to d goes through i
                            if (mode == 1) continue;             // split horizon: j does not advertise it
                            if (mode == 2) adv = INF;            // poison reverse: j advertises INF
                        }
                        int nc = Math.min(INF, cost[i][j] + adv);
                        if ((s.next[i][d] == j && nc != s.dist[i][d]) || nc < s.dist[i][d]) {
                            if (nc >= INF && s.dist[i][d] >= INF) continue;
                            s.dist[i][d] = nc; s.next[i][d] = nc >= INF ? -1 : j;
                            changed = true;
                            log.append(String.format("  R%d: to R%d cost %s via %s%n", i + 1, d + 1,
                                    nc >= INF ? "INF" : String.valueOf(nc), nc >= INF ? "-" : "R" + (j + 1)));
                            if (nc < INF && (old.dist[i][d] >= INF || nc > old.dist[i][d])) bad[i][d]++;
                        }
                    }
                }
            }
            if (!changed) break;
            rounds++;
            if (verbose) System.out.print("Round " + rounds + ":\n" + log);
        }
        for (int i = 0; i < n; i++)                        // climbed >= 2 times and ended unreachable
            for (int d = 0; d < n; d++) if (bad[i][d] >= 2 && s.dist[i][d] >= INF) cti = true;
        return new int[]{rounds, msgs, cti ? 1 : 0};
    }

    static void print(State s) {
        for (int i = 0; i < n; i++) {
            System.out.println("\nRouting table of Router " + (i + 1));
            System.out.println("Dest   Cost   NextHop");
            for (int d = 0; d < n; d++)
                System.out.printf("R%-5d %-6s %s%n", d + 1, s.dist[i][d] >= INF ? "INF" : String.valueOf(s.dist[i][d]),
                        d == i ? "-" : s.next[i][d] < 0 ? "unreachable" : "R" + (s.next[i][d] + 1));
        }
    }
}
