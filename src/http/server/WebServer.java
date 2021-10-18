///A Simple Web Server (WebServer.java)

package http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
                PrintWriter out = new PrintWriter(remote.getOutputStream());

                //ArrayList<String> str = parseRequest(in);
                Request r = new Request(in);
                System.out.println(r);
                printResource(r.uri, out);

                out.flush();
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

    public static ArrayList<String> parseRequest(BufferedReader in) throws IOException {
        ArrayList<String> al = new ArrayList<String>();
        String str = in.readLine();
        while (!str.equals("")) {
            al.add(str);
            System.out.println(str);
            str = in.readLine();
        }
        return al;
    }

    public static void printResource(String path, PrintWriter out) throws FileNotFoundException {

        // Send the response
        // Send the headers
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("Server: Bot");
        // this blank line signals the end of the headers
        out.println("");

        File myObj;
        Scanner myReader;
        try {
            myObj = new File("html/" + path);
            myReader = new Scanner(myObj);
        } catch (IOException e) {
            myObj = new File("html/index.html");
            myReader = new Scanner(myObj);
        }
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            out.println(data);
        }
        myReader.close();
    }
}
