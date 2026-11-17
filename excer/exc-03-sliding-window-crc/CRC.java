import java.util.Scanner;

public class CRC {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        int data[], divisor[], remainder[], total[];

        System.out.print("Enter no. of data bits : ");
        int n = sc.nextInt();

        System.out.print("Enter no. of generator bits : ");
        int m = sc.nextInt();

        data = new int[n];
        divisor = new int[m];
        total = new int[n + m - 1];
        remainder = new int[n + m - 1];

        System.out.println("Enter data bits :");
        for (int i = 0; i < n; i++) {
            data[i] = sc.nextInt();
            total[i] = data[i];
        }

        System.out.println("Enter generator bits :");
        for (int i = 0; i < m; i++) {
            divisor[i] = sc.nextInt();
        }

        for (int i = n; i < total.length; i++) {
            total[i] = 0;
        }

        for (int i = 0; i < total.length; i++) {
            remainder[i] = total[i];
        }

        for (int i = 0; i < n; i++) {
            if (remainder[i] == 1) {
                for (int j = 0; j < m; j++) {
                    remainder[i + j] ^= divisor[j];
                }
            }
        }

        System.out.print("\nThe code bits added are : ");
        for (int i = n; i < total.length; i++) {
            System.out.print(remainder[i]);
        }

        System.out.print("\n\nThe code data is : ");
        for (int i = 0; i < n; i++) {
            System.out.print(data[i]);
        }
        for (int i = n; i < total.length; i++) {
            System.out.print(remainder[i]);
        }
        System.out.println();

        sc.close();
    }
}
