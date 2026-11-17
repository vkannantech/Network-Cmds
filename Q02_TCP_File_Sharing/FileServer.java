import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.zip.CRC32;

// Q2 - Multi-client TCP file-sharing server
// Commands from client: LIST | GET <file> | PUT <file> | QUIT
public class FileServer {
    static final int PORT = 6000;
    static final File SHARED = new File("shared");

    public static void main(String[] args) throws IOException {
        SHARED.mkdirs();
        ExecutorService pool = Executors.newFixedThreadPool(10);   // scalability: max 10 clients at once
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("File Server started on TCP port " + PORT + ", sharing folder: " + SHARED.getAbsolutePath());
        int id = 0;
        while (true) {
            Socket s = ss.accept();                                // wait for a client
            pool.execute(new ClientHandler(s, ++id));              // each client in its own thread
        }
    }

    static long crc(byte[] b) { CRC32 c = new CRC32(); c.update(b); return c.getValue(); }

    static class ClientHandler implements Runnable {
        Socket s; int id;
        ClientHandler(Socket s, int id) { this.s = s; this.id = id; }

        public void run() {
            String who = "Client-" + id + " " + s.getRemoteSocketAddress();
            System.out.println("[+] Connected   " + who + "  (thread " + Thread.currentThread().getName() + ")");
            try (DataInputStream in = new DataInputStream(s.getInputStream());
                 DataOutputStream out = new DataOutputStream(s.getOutputStream())) {
                s.setSoTimeout(600000);    // drop idle/dead clients after 10 min
                s.setKeepAlive(true);      // detect broken connections
                while (true) {
                    String cmd = in.readUTF();
                    System.out.println("    " + who + " -> " + cmd);
                    if (cmd.equals("LIST")) {
                        String[] names = SHARED.list();
                        out.writeUTF(names.length == 0 ? "(no files)" : String.join("\n", names));
                    } else if (cmd.startsWith("GET ")) {
                        File f = new File(SHARED, new File(cmd.substring(4)).getName()); // block ../ paths
                        if (!f.isFile()) { out.writeUTF("ERROR File not found"); continue; }
                        byte[] data = java.nio.file.Files.readAllBytes(f.toPath());
                        out.writeUTF("OK");
                        out.writeLong(data.length);
                        out.write(data);
                        out.writeLong(crc(data));                  // checksum for integrity
                        System.out.println("    sent " + f.getName() + " (" + data.length + " bytes, CRC32=" + Long.toHexString(crc(data)) + ")");
                    } else if (cmd.startsWith("PUT ")) {
                        String name = new File(cmd.substring(4)).getName();
                        byte[] data = new byte[(int) in.readLong()];
                        in.readFully(data);
                        long sentCrc = in.readLong();
                        if (crc(data) != sentCrc) { out.writeUTF("ERROR Checksum mismatch - resend"); continue; }
                        synchronized (FileServer.class) {         // two clients writing at once
                            java.nio.file.Files.write(new File(SHARED, name).toPath(), data);
                        }
                        out.writeUTF("OK Stored " + name + " (" + data.length + " bytes, checksum verified)");
                        System.out.println("    received " + name + " (" + data.length + " bytes) checksum OK");
                    } else if (cmd.equals("QUIT")) {
                        out.writeUTF("BYE"); break;
                    } else {
                        out.writeUTF("ERROR Unknown command");
                    }
                }
            } catch (EOFException e) {
                System.out.println("    " + who + " closed the connection");
            } catch (IOException e) {
                System.out.println("    " + who + " error: " + e.getMessage());
            } finally {
                try { s.close(); } catch (IOException ignored) {}
                System.out.println("[-] Disconnected " + who);
            }
        }
    }
}
