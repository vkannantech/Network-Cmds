import java.util.Scanner;

public class LinkState {
    static int minDistance(int dist[], boolean visited[], int n) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < n; i++) {
            if (!visited[i] && dist[i] < min) {
                min = dist[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of nodes: ");
        int n = sc.nextInt();

        int graph[][] = new int[n][n];

        System.out.println("Enter cost matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = sc.nextInt();
            }
        }

        System.out.print("Enter source node (0 to " + (n - 1) + "): ");
        int source = sc.nextInt();

        int dist[] = new int[n];
        boolean visited[] = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        dist[source] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = minDistance(dist, visited, n);

            if (u == -1) break;

            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (!visited[v] &&
                    graph[u][v] != 0 &&
                    dist[u] != Integer.MAX_VALUE &&
                    dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }

        System.out.println("\nShortest distances from source:");
        for (int i = 0; i < n; i++) {
            System.out.println("To Node " + i + " Distance = " + dist[i]);
        }

        sc.close();
    }
}
