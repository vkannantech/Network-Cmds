import java.io.*;
import java.net.*;

public class Server {
    public static void main(String args[]) {
        ServerSocket ss = null;
        Socket s = null;
        try {
            ss = new ServerSocket(5000);
            System.out.println("=================================================");
            System.out.println("  TCP Server listening on Port 5000");
            System.out.println("=================================================");
            System.out.println("Server waiting for connection...\n");

            s = ss.accept();
            System.out.println("Client connected from: " + s.getRemoteSocketAddress());

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            String message = dis.readUTF();
            System.out.println("Message from Client: " + message);

            String response = "Message received successfully";
            dos.writeUTF(response);
            System.out.println("Response sent: \"" + response + "\"");

            dis.close();
            dos.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (s != null && !s.isClosed()) s.close();
                if (ss != null && !ss.isClosed()) ss.close();
            } catch (IOException ignored) {}
            System.out.println("\nConnection closed. Server finished.");
        }
    }
}
