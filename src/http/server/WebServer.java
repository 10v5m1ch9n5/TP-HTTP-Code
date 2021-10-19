///A Simple Web Server (WebServer.java)

package http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * <p>
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

    /**
     * WebServer constructor.
     */
    protected void start() {
        ServerSocket s;

        System.out.println("Webserver starting up on port 3000");
        System.out.println("(press ctrl-c to exit)");
        try {
            // create the main server socket
            s = new ServerSocket(3000);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return;
        }

        System.out.println("Waiting for connection");
        for (; ; ) {
            try {
                // wait for a connection
                Socket remote = s.accept();
                // remote is now the connected socket
                System.out.println("Connection, sending data.");
                BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
                //PrintWriter out = new PrintWriter(remote.getOutputStream());
                OutputStream os = remote.getOutputStream();

                Request r = new Request(in);
                System.out.println(r);
                if(r.uri.equals("/")) {
                    resolve("index.html", os);
                } else {
                    resolve(r.uri.substring(1), os);
                }

                os.flush();
                remote.close();

            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    /**
     * Start the application.
     *
     * @param args Command line parameters are not used.
     */
    public static void main(String args[]) {
        WebServer ws = new WebServer();
        ws.start();
    }

    public static void resolve(String resourcePath, OutputStream os) throws IOException {
        File f = new File(resourcePath);
        String extension = resourcePath.split("\\.")[1];
        if (extension.equals("html")) {
            f = new File("html/" + resourcePath);
        }
        StringBuilder response = new StringBuilder();
        if (!f.exists()) {
            response.append("HTTP/1.0 404 Not Found\r\n\r\n");
            System.out.println("404 NOT FOUND " + resourcePath);
            os.write(response.toString().getBytes(StandardCharsets.UTF_8));
            return;
        }
        response.append("HTTP/1.0 200 OK\r\n");
        response.append("Content-Type: ");
        response.append(getMIMEType(extension));
        response.append("\r\n");
        response.append("Server: Bot\r\n\r\n");
        os.write(response.toString().getBytes(StandardCharsets.UTF_8));

        FileInputStream fis = new FileInputStream(f);
        byte[] data = new byte[(int) f.length()];
        fis.read(data);
        fis.close();
        os.write(data);
        os.write("\r\n\r\n".getBytes(StandardCharsets.UTF_8));
    }

    public static String getMIMEType(String extension) {
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            return "image/jpeg";
        } else if (extension.equals("html") || extension.equals("htm")) {
            return "text/html";
        } else {
            return null;
        }
    }
}
