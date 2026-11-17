import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) {
        Socket s = null;
        Scanner sc = null;
        try {
            System.out.println("Connecting to TCP Server on localhost:5000...");
            s = new Socket("localhost", 5000);
            System.out.println("Connected to server successfully!");

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            sc = new Scanner(System.in);
            System.out.print("Enter message: ");
            String msg = sc.nextLine();

            dos.writeUTF(msg);
            dos.flush();

            String response = dis.readUTF();
            System.out.println("Server Response: " + response);

            dis.close();
            dos.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (sc != null) sc.close();
            try {
                if (s != null && !s.isClosed()) s.close();
            } catch (IOException ignored) {}
            System.out.println("Client finished and connection closed.");
        }
    }
}
