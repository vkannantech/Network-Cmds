import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.zip.CRC32;

// Q2 - TCP file-sharing client.  Usage: java FileClient [serverIP]
public class FileClient {
    static long crc(byte[] b) { CRC32 c = new CRC32(); c.update(b); return c.getValue(); }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        Socket s = new Socket();
        s.connect(new InetSocketAddress(host, 6000), 5000);      // 5 s connect timeout
        System.out.println("Connected to File Server " + host + ":6000");
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        Scanner sc = new Scanner(System.in);
        new File("downloads").mkdirs();

        while (true) {
            System.out.print("\nCommand (LIST / GET <file> / PUT <file> / QUIT): ");
            if (!sc.hasNextLine()) break;
            String cmd = sc.nextLine().trim();
            if (cmd.isEmpty()) continue;

            if (cmd.startsWith("PUT ")) {
                File f = new File(cmd.substring(4));
                if (!f.isFile()) { System.out.println("Local file not found: " + f); continue; }
                byte[] data = java.nio.file.Files.readAllBytes(f.toPath());
                out.writeUTF("PUT " + f.getName());
                out.writeLong(data.length);
                out.write(data);
                out.writeLong(crc(data));
                System.out.println("Server: " + in.readUTF());
            } else if (cmd.startsWith("GET ")) {
                out.writeUTF(cmd);
                String status = in.readUTF();
                if (!status.equals("OK")) { System.out.println("Server: " + status); continue; }
                byte[] data = new byte[(int) in.readLong()];
                in.readFully(data);                               // TCP gives all bytes, in order
                long serverCrc = in.readLong();
                String name = new File(cmd.substring(4)).getName();
                if (crc(data) == serverCrc) {
                    java.nio.file.Files.write(new File("downloads", name).toPath(), data);
                    System.out.println("Downloaded " + name + " (" + data.length + " bytes) -> downloads/" + name);
                    System.out.println("Integrity check: CRC32 " + Long.toHexString(serverCrc) + " MATCHED");
                } else {
                    System.out.println("Integrity check FAILED - file corrupted, please GET again");
                }
            } else {
                out.writeUTF(cmd);
                String reply = in.readUTF();
                System.out.println("Server: " + (cmd.equals("LIST") ? "\n" : "") + reply);
                if (cmd.equals("QUIT")) break;
            }
        }
        s.close();
        System.out.println("Connection closed.");
    }
}
