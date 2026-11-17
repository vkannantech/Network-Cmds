import java.io.*;
import java.net.*;

public class Serverdns12 {
    public static void main(String args[]) {
        DatagramSocket server = null;
        try {
            server = new DatagramSocket(1309);
            System.out.println("=================================================");
            System.out.println("  DNS Server running on UDP Port 1309");
            System.out.println("  Listening for queries...");
            System.out.println("=================================================");

            String ip[] = {
                "165.165.80.80",
                "165.165.79.1"
            };

            String name[] = {
                "www.aptitudeguru.com",
                "www.downloadcyclone.blogspot.com"
            };

            while (true) {
                byte receivebyte[] = new byte[1024];
                DatagramPacket receiver = new DatagramPacket(receivebyte, receivebyte.length);
                server.receive(receiver);

                String str = new String(receiver.getData(), 0, receiver.getLength());
                String s = str.trim();

                InetAddress clientAddr = receiver.getAddress();
                int clientPort = receiver.getPort();

                System.out.println("\nRequest received from client (" + clientAddr.getHostAddress() + ":" + clientPort + "): \"" + s + "\"");
                System.out.println("Searching records...");

                String response = "Not Found";
                boolean found = false;

                for (int i = 0; i < ip.length; i++) {
                    if (s.equalsIgnoreCase(ip[i])) {
                        response = name[i];
                        found = true;
                        break;
                    } else if (s.equalsIgnoreCase(name[i])) {
                        response = ip[i];
                        found = true;
                        break;
                    }
                }

                if (found) {
                    System.out.println("Match found -> " + response);
                } else {
                    System.out.println("Record not found for query: " + s);
                }

                byte sendbyte[] = response.getBytes();
                DatagramPacket sender = new DatagramPacket(sendbyte, sendbyte.length, clientAddr, clientPort);
                server.send(sender);
                System.out.println("Response sent successfully");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (server != null && !server.isClosed()) {
                server.close();
            }
        }
    }
}
