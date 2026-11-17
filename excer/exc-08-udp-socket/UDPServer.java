import java.net.*;

public class UDPServer {
    public static void main(String args[]) {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(5000);
            System.out.println("=================================================");
            System.out.println("  UDP Server listening on Port 5000");
            System.out.println("=================================================");
            System.out.println("Server waiting...\n");

            byte receive[] = new byte[1024];
            DatagramPacket dpReceive = new DatagramPacket(receive, receive.length);

            ds.receive(dpReceive);

            String message = new String(dpReceive.getData(), 0, dpReceive.getLength());
            System.out.println("Message from Client: " + message);

            String response = "Message received successfully";
            byte send[] = response.getBytes();

            DatagramPacket dpSend = new DatagramPacket(
                send,
                send.length,
                dpReceive.getAddress(),
                dpReceive.getPort()
            );

            ds.send(dpSend);
            System.out.println("Response sent to " + dpReceive.getAddress().getHostAddress() + ":" + dpReceive.getPort());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (ds != null && !ds.isClosed()) {
                ds.close();
            }
            System.out.println("\nServer socket closed. Server finished.");
        }
    }
}
