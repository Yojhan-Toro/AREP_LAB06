package Arep.Lab06;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    static Map<String, WebMethod> endPoints = new HashMap<>();
    static String staticFilesFolder = "webroot/public";

    static Map<String, String> MIME_TYPES = new HashMap<>();
    static {
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("css",  "text/css");
        MIME_TYPES.put("js",   "application/javascript");
        MIME_TYPES.put("png",  "image/png");
        MIME_TYPES.put("jpg",  "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {

            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            String inputLine;
            boolean isFirstLine = true;
            String repath = null;
            String struripath = null;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);

                if (isFirstLine) {
                    String[] flTokens = inputLine.split(" ");
                    struripath = flTokens[1];
                    URI uripath = new URI(struripath);
                    repath = uripath.getPath();
                    System.out.println("Path: " + repath);
                    isFirstLine = false;
                }

                if (!in.ready()) break;
            }

            WebMethod currentWm = endPoints.get(repath);
            String outputLine;

            if (currentWm != null) {
                HttpRequest req = new HttpRequest(struripath);
                HttpResponse res = new HttpResponse();

                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>"
                        + "<html><head><meta charset=\"UTF-8\"><title>Response</title></head>"
                        + "<body>"
                        + currentWm.execute(req, res)
                        + "</body></html>";

            } else {

                String resourcePath = staticFilesFolder + repath;
                InputStream fileStream = HttpServer.class.getClassLoader()
                        .getResourceAsStream(resourcePath);

                if (fileStream != null) {
                    String ext = repath.contains(".") ? repath.substring(repath.lastIndexOf('.') + 1) : "";
                    String contentType = MIME_TYPES.getOrDefault(ext, "text/html");
                    byte[] fileBytes = fileStream.readAllBytes();
                    fileStream.close();

                    String headers = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: " + contentType + "\r\n"
                            + "\r\n";
                    clientSocket.getOutputStream().write(headers.getBytes());
                    clientSocket.getOutputStream().write(fileBytes);
                    out.close();
                    in.close();
                    clientSocket.close();
                    continue;
                }

                outputLine = "HTTP/1.1 404 Not Found\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>"
                        + "<html><body><h1>404 - Not Found</h1></body></html>";
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static void get(String path, WebMethod wm) {
        endPoints.put(path, wm);
    }

    public static void staticfiles(String folder) {
        staticFilesFolder = folder.startsWith("/") ? folder.substring(1) : folder;
    }
}
