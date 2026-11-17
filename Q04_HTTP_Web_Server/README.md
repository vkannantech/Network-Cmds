# Q4 – Lightweight Multi-User HTTP Web Server

## Question
> A lightweight web server must handle basic **HTTP GET** requests and serve **static HTML**. Design the server to **support multiple users, handle status codes, and allow future features**. Explain how **HTTP compliance, resource management, and concurrency** are achieved.

## Aim
To implement an HTTP/1.1 web server in Java that serves static files from `www/`, handles many browsers at once with a thread pool, returns correct status codes, and has a route table so new features can be added easily.

## Apparatus Required
Java JDK 8 or later, a web browser (and optionally `curl`). Files: `WebServer.java`, `www/index.html`, `www/about.html`, `www/style.css`.

## Features
| Feature | Implementation |
|---|---|
| Methods | `GET` (headers + body), `HEAD` (headers only) |
| Status codes | **200** OK, **400** Bad Request, **403** Forbidden (`../` path traversal), **404** Not Found, **405** Method Not Allowed (POST/PUT…), **505** HTTP Version Not Supported |
| Headers | `Date`, `Server`, `Content-Type` (by extension), `Content-Length`, `Connection: close` |
| Multi-user | Fixed thread pool of 20 workers |
| Future features | `ROUTES` map: `/status` and `/hello?name=` are dynamic pages. Add one line to add a new one |

## Algorithm
1. Start. Create `ServerSocket(8080)` and a thread pool of 20.
2. Loop: `accept()` a connection and give it to a pool thread.
3. Worker: set a 10 s timeout and read the **request line** `METHOD PATH VERSION`, then skip the headers up to the blank line.
4. Validate in order: format → 400; version not HTTP/1.x → 505; method not GET/HEAD → 405.
5. If the path is in `ROUTES`, run that handler → 200.
6. Otherwise map the path to `www/` (`/` → `index.html`). If it resolves outside `www` → 403; file missing → 404; else read it → 200 with the right Content-Type.
7. Send the status line, headers, a blank line, and the body (not for HEAD). Close the socket and log the request.

## Flow chart
```
 Browser ---- GET /about.html HTTP/1.1 ----> accept() -> thread pool
                                                  |
                         parse request line --- bad? --> 400
                                                  | version? --> 505
                                                  | method?  --> 405
                                    route in ROUTES? --yes--> dynamic 200
                                                  | no
                                    path outside www? --yes--> 403
                                    file exists?      --no---> 404
                                                  | yes
 Browser <--- HTTP/1.1 200 OK + headers + file ---+
```

## How to Run
| | Windows | Linux |
|---|---|---|
| Start server | `run_server.bat` | `./run_server.sh` |
| Other port | `run_server.bat 9090` | `./run_server.sh 9090` |

Open **http://localhost:8080/** in a browser. Click the links to see 200, 404 and the dynamic routes.
Test with curl (Windows 10+ includes curl):
```
curl -i http://localhost:8080/
curl -i http://localhost:8080/missing.html
curl -i -X POST http://localhost:8080/
curl -I http://localhost:8080/
curl --path-as-is http://localhost:8080/../../etc/passwd
```
> Port 8080 is used (not 80) because ports below 1024 need admin/root rights on Linux.

## Sample Output (verified)
**Status code tests**
```
/                      200 text/html 549B
/about.html            200 text/html 325B
/style.css             200 text/css 134B
/missing.html          404 text/html 114B
/hello?name=Kannan     200 text/html 22B
/status                200 text/html 71B
/../../etc/passwd      403 text/html 85B
POST                   405
HEAD                   HTTP/1.1 200 OK
bad request            HTTP/1.1 400 Bad Request
-- 50 concurrent requests:
     50 200
```
**Server log**
```
Web Server running at http://localhost:8080/  (document root: .../Q04_HTTP_Web_Server/www)
[pool-1-thread-1] 127.0.0.1  "GET / HTTP/1.1"  -> 200
[pool-1-thread-2] 127.0.0.1  "GET /missing.html HTTP/1.1"  -> 404
[pool-1-thread-3] 127.0.0.1  "GET /hello?name=Kannan HTTP/1.1"  -> 200
[pool-1-thread-4] 127.0.0.1  "GET /../../etc/passwd HTTP/1.1"  -> 403
[pool-1-thread-5] 127.0.0.1  "POST / HTTP/1.1"  -> 405
[pool-1-thread-6] 127.0.0.1  "GARBAGE"  -> 400
```
**Raw response**
```
HTTP/1.1 200 OK
Date: Tue, 17 Nov 2026 18:26:23 GMT
Server: LabWebServer/1.0
Content-Type: text/html
Content-Length: 549
Connection: close

<!DOCTYPE html> ...
```

## Explanation (answer to "Explain")
**HTTP compliance**
- Parses the request line exactly as RFC 9112 defines it (`method SP target SP version`) and reads headers up to the empty line.
- Responses use the correct status line, CRLF line endings, a blank line before the body, and the required headers (`Content-Length`, `Content-Type`, `Date`).
- Correct status codes: 200, 400, 403, 404, 405, 505. HEAD returns the same headers as GET but no body.

**Resource management**
- A **bounded thread pool** (20 threads) instead of unlimited threads, so a traffic spike can't exhaust memory.
- **try-with-resources** closes every socket and stream, even on errors. There are no leaks.
- `setSoTimeout(10000)` drops slow or idle clients so they don't hold a thread forever.
- `Connection: close`: each request is completed and its resources freed immediately.
- **Security:** the path is normalised and must stay inside `www/` (blocks `../` path traversal → 403).

**Concurrency**
- The main thread only accepts connections; workers handle requests in parallel. The test sent 50 simultaneous requests and all returned 200.
- The shared counter `requests` is updated inside `synchronized`, so there's no race condition.

**Future features (extensibility)**
- The `ROUTES` map links a path to a `Handler` (lambda). To add a feature, add one line, e.g. `ROUTES.put("/time", q -> new Date().toString());`
- MIME types live in one `type()` method. POST support, keep-alive or HTTPS can be added without changing the core loop.

## Viva Questions
1. **What is the difference between GET and HEAD?** HEAD returns only the headers, no body. It's used to check whether a resource exists or has changed.
2. **What do 404 and 405 mean?** 404: the resource doesn't exist. 405: the method isn't allowed for this resource.
3. **Why use a thread pool?** It reuses threads and limits their number, so performance stays predictable under load.
4. **What is Content-Length used for?** It tells the client where the body ends.
5. **Which transport protocol does HTTP use, and which port?** TCP; port 80 (HTTPS uses 443).
6. **What is a path traversal attack?** Requesting `../../file` to escape the web root. It's blocked here by normalising the path and checking the prefix.

## Result
A lightweight multi-threaded HTTP web server was implemented. It served static HTML to many users at once with correct status codes and headers, managed resources safely, and supports adding new routes easily.
