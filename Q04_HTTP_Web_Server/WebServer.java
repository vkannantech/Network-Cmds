import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

// Q4 - Lightweight multi-threaded HTTP/1.1 server serving static HTML from ./www
// Usage: java WebServer [port]   (default 8080)  ->  open http://localhost:8080/
public class WebServer {
    static final Path ROOT = Paths.get("www").toAbsolutePath().normalize();
    static int requests = 0;

    // "future features": add a new dynamic page by adding one entry to this map
    interface Handler { String handle(Map<String, String> query); }
    static final Map<String, Handler> ROUTES = new HashMap<>();
    static {
        ROUTES.put("/status", q -> "<h1>Server Status</h1><p>Requests served: " + requests
                + "</p><p>Active threads: " + Thread.activeCount() + "</p>");
        ROUTES.put("/hello", q -> "<h1>Hello " + q.getOrDefault("name", "Guest") + "!</h1>");
    }

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        ExecutorService pool = Executors.newFixedThreadPool(20);   // concurrency, bounded resources
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Web Server running at http://localhost:" + port + "/  (document root: " + ROOT + ")");
        while (true) {
            Socket s = ss.accept();
            pool.execute(() -> handle(s));
        }
    }

    static void handle(Socket s) {
        try (Socket sock = s; BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             OutputStream out = s.getOutputStream()) {
            s.setSoTimeout(10000);                       // don't let slow clients hold a thread forever
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;
            String h; while ((h = in.readLine()) != null && !h.isEmpty()) { }  // skip headers

            synchronized (WebServer.class) { requests++; }
            String[] parts = requestLine.split(" ");
            int code;
            if (parts.length != 3)                          code = send(out, 400, "Bad Request", "text/html", page(400, "Malformed request line"), true);
            else if (!parts[2].startsWith("HTTP/1."))       code = send(out, 505, "HTTP Version Not Supported", "text/html", page(505, parts[2]), true);
            else if (!parts[0].equals("GET") && !parts[0].equals("HEAD"))
                                                            code = send(out, 405, "Method Not Allowed", "text/html", page(405, parts[0] + " is not supported"), true);
            else code = serve(out, parts[1], parts[0].equals("GET"));

            System.out.printf("[%s] %s  \"%s\"  -> %d%n", Thread.currentThread().getName(),
                    s.getInetAddress().getHostAddress(), requestLine, code);
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    static int serve(OutputStream out, String target, boolean withBody) throws IOException {
        String path = target, qs = "";
        int qi = target.indexOf('?');
        if (qi >= 0) { path = target.substring(0, qi); qs = target.substring(qi + 1); }
        path = URLDecoder.decode(path, "UTF-8");

        if (ROUTES.containsKey(path)) {                                  // dynamic route
            Map<String, String> q = new HashMap<>();
            for (String kv : qs.split("&")) { String[] p = kv.split("=", 2); if (p.length == 2) q.put(p[0], p[1]); }
            return send(out, 200, "OK", "text/html", ROUTES.get(path).handle(q).getBytes(), withBody);
        }
        if (path.endsWith("/")) path += "index.html";
        Path file = ROOT.resolve(path.substring(1)).normalize();
        if (!file.startsWith(ROOT))                                      // blocks /../../etc/passwd
            return send(out, 403, "Forbidden", "text/html", page(403, "Access denied"), withBody);
        if (!Files.isRegularFile(file))
            return send(out, 404, "Not Found", "text/html", page(404, path + " was not found on this server"), withBody);
        return send(out, 200, "OK", type(file.toString()), Files.readAllBytes(file), withBody);
    }

    static int send(OutputStream out, int code, String msg, String type, byte[] body, boolean withBody) throws IOException {
        SimpleDateFormat f = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        f.setTimeZone(TimeZone.getTimeZone("GMT"));
        String head = "HTTP/1.1 " + code + " " + msg + "\r\n"
                + "Date: " + f.format(new Date()) + "\r\n"
                + "Server: LabWebServer/1.0\r\n"
                + "Content-Type: " + type + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "Connection: close\r\n\r\n";
        out.write(head.getBytes());
        if (withBody) out.write(body);
        out.flush();
        return code;
    }

    static byte[] page(int code, String text) {
        return ("<html><body><h1>" + code + "</h1><p>" + text + "</p><hr><i>LabWebServer/1.0</i></body></html>").getBytes();
    }

    static String type(String n) {
        n = n.toLowerCase();
        if (n.endsWith(".html") || n.endsWith(".htm")) return "text/html";
        if (n.endsWith(".css")) return "text/css";
        if (n.endsWith(".js")) return "application/javascript";
        if (n.endsWith(".png")) return "image/png";
        if (n.endsWith(".jpg") || n.endsWith(".jpeg")) return "image/jpeg";
        if (n.endsWith(".txt")) return "text/plain";
        return "application/octet-stream";
    }
}
