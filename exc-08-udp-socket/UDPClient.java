import java.net.*;
import java.util.Scanner;

public class UDPClient {
    public static void main(String args[]) {
        DatagramSocket ds = null;
        Scanner sc = null;
        try {
            ds = new DatagramSocket();
            ds.setSoTimeout(5000);

            InetAddress ip = InetAddress.getByName("localhost");

            sc = new Scanner(System.in);
            System.out.print("Enter message: ");
            String msg = sc.nextLine();

            byte send[] = msg.getBytes();

            DatagramPacket dpSend = new DatagramPacket(
                send,
                send.length,
                ip,
                5000
            );

            System.out.println("Sending datagram packet to localhost:5000...");
            ds.send(dpSend);

            byte receive[] = new byte[1024];
            DatagramPacket dpReceive = new DatagramPacket(receive, receive.length);

            ds.receive(dpReceive);

            String response = new String(dpReceive.getData(), 0, dpReceive.getLength());
            System.out.println("Server Response: " + response);
        } catch (SocketTimeoutException ste) {
            System.out.println("Timeout: Server did not respond within 5 seconds. Ensure UDPServer is running.");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (sc != null) sc.close();
            if (ds != null && !ds.isClosed()) {
                ds.close();
            }
            System.out.println("Client socket closed.");
        }
    }
}
