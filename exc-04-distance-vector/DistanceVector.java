import java.util.Scanner;

public class DistanceVector {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        int nodes;

        System.out.print("Enter the number of nodes: ");
        nodes = sc.nextInt();

        int cost[][] = new int[nodes][nodes];
        int distance[][] = new int[nodes][nodes];

        System.out.println("Enter the cost matrix:");

        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                cost[i][j] = sc.nextInt();
                distance[i][j] = cost[i][j];
            }
        }

        for (int k = 0; k < nodes; k++) {
            for (int i = 0; i < nodes; i++) {
                for (int j = 0; j < nodes; j++) {
                    if (distance[i][j] > distance[i][k] + distance[k][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }

        System.out.println("\nRouting Table:");

        for (int i = 0; i < nodes; i++) {
            System.out.println("\nFor Router " + (i + 1));

            for (int j = 0; j < nodes; j++) {
                System.out.println("Node " + (j + 1) + " Distance = " + distance[i][j]);
            }
        }

        sc.close();
    }
}
