import java.io.*;
import java.net.*;

public class Clientdns12 {
    public static void main(String args[]) {
        DatagramSocket client = null;
        try {
            client = new DatagramSocket();
            client.setSoTimeout(5000);

            InetAddress addr = InetAddress.getByName("127.0.0.1");

            byte sendbyte[] = new byte[1024];
            byte receivebyte[] = new byte[1024];

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter DOMAIN NAME or IP Address:");
            String str = in.readLine();

            if (str == null || str.trim().isEmpty()) {
                System.out.println("No input provided. Exiting.");
                return;
            }

            str = str.trim();
            sendbyte = str.getBytes();

            DatagramPacket sender = new DatagramPacket(sendbyte, sendbyte.length, addr, 1309);
            client.send(sender);

            DatagramPacket receiver = new DatagramPacket(receivebyte, receivebyte.length);
            client.receive(receiver);

            String s = new String(receiver.getData(), 0, receiver.getLength());
            System.out.println("\nIP Address/Domain Name: " + s.trim());
        } catch (SocketTimeoutException ste) {
            System.out.println("Timeout: DNS Server did not respond within 5 seconds. Ensure Serverdns12 is running.");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (client != null && !client.isClosed()) {
                client.close();
            }
        }
    }
}
