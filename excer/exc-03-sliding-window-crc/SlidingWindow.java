import java.util.Scanner;

public class SlidingWindow {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        int frames, window, i;

        System.out.print("Enter number of frames: ");
        frames = sc.nextInt();

        System.out.print("Enter window size: ");
        window = sc.nextInt();

        System.out.println();
        for (i = 1; i <= frames; i++) {
            System.out.println("Frame " + i + " sent");

            if (i % window == 0) {
                System.out.println("Acknowledgement received for frames up to " + i);
            }
        }

        if (frames % window != 0) {
            System.out.println("Acknowledgement received for remaining frames");
        }

        System.out.println("\nAll frames transmitted successfully");
        sc.close();
    }
}
