import java.util.Scanner;

public class ARPRARP {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        String ip[] = {
            "192.168.1.1",
            "192.168.1.2",
            "192.168.1.3"
        };

        String mac[] = {
            "AA:BB:CC:11:22:33",
            "AA:BB:CC:44:55:66",
            "AA:BB:CC:77:88:99"
        };

        int choice;

        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. ARP");
            System.out.println("2. RARP");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                break;
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("\nEnter IP Address: ");
                    String inputIP = sc.next().trim();
                    boolean foundIP = false;

                    for (int i = 0; i < ip.length; i++) {
                        if (inputIP.equalsIgnoreCase(ip[i])) {
                            System.out.println("MAC Address: " + mac[i]);
                            foundIP = true;
                            break;
                        }
                    }

                    if (!foundIP) {
                        System.out.println("IP Address not found in ARP table");
                    }
                    break;

                case 2:
                    System.out.print("\nEnter MAC Address: ");
                    String inputMAC = sc.next().trim();
                    boolean foundMAC = false;

                    for (int i = 0; i < mac.length; i++) {
                        if (inputMAC.equalsIgnoreCase(mac[i])) {
                            System.out.println("IP Address: " + ip[i]);
                            foundMAC = true;
                            break;
                        }
                    }

                    if (!foundMAC) {
                        System.out.println("MAC Address not found in RARP table");
                    }
                    break;

                case 3:
                    System.out.println("\nProgram terminated");
                    break;

                default:
                    System.out.println("\nInvalid choice");
            }

        } while (choice != 3);

        sc.close();
    }
}
