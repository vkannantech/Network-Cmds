import java.net.*;
import java.util.Scanner;

// Q7 - Diskless client: knows only its MAC, asks the RARP server for its IP.
// Usage: java RARPClient [serverIP]      then type MAC addresses, "exit" to quit
public class RARPClient {
    public static void main(String[] args) throws Exception {
        InetAddress server = InetAddress.getByName(args.length > 0 ? args[0] : "localhost");
        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(2000);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("\nEnter MAC Address of diskless client (exit to quit): ");
            if (!sc.hasNextLine()) break;
            String mac = sc.nextLine().trim();
            if (mac.isEmpty()) continue;
            if (mac.equalsIgnoreCase("exit")) break;

            byte[] out = ("RARP_REQUEST|" + mac).getBytes();
            String reply = null;
            for (int t = 1; t <= 3 && reply == null; t++) {      // UDP: retry if no answer
                System.out.println("Broadcasting RARP request: \"What is my IP? My MAC is " + mac + "\" (attempt " + t + ")");
                ds.send(new DatagramPacket(out, out.length, server, 5400));
                try {
                    byte[] buf = new byte[256];
                    DatagramPacket p = new DatagramPacket(buf, buf.length);
                    ds.receive(p);
                    reply = new String(p.getData(), 0, p.getLength());
                } catch (SocketTimeoutException e) { System.out.println("  no reply..."); }
            }
            if (reply == null) { System.out.println("RARP server not available - client cannot boot"); continue; }
            String[] f = reply.split("\\|");
            if (f[0].equals("RARP_REPLY")) System.out.println("Assigned IP Address: " + f[2] + "   [" + f[3] + "]");
            else                           System.out.println("No IP assigned: " + f[2]);
        }
        ds.close();
    }
}
