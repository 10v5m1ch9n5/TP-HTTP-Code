package http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Request {
    public String method;
    public String http_ver;
    public String uri;
    public HashMap<String, String> headers;
    public HashMap<String, String> reqParams;
    public HashMap<String, String> body;

    Request(BufferedReader in) throws IOException {
        String str;
        headers = new HashMap<String, String>();
        body = new HashMap<String, String>();
        reqParams = new HashMap<>();

        str = in.readLine();
        if (str == null)
            return;
        method = str.split(" ")[0];
        uri = str.split(" ")[1];
        http_ver = str.split(" ")[2];

        if (uri.contains("?")) {
            String[] par = uri.split("\\?")[1].split("&");
            uri = uri.split("\\?")[0];
            System.out.println("\nParamètres HTTP :");
            for (String s : par) {
                System.out.println(s.split("=")[0] + "=" + s.split("=")[1]);
            }
            System.out.println();
        }

        // header parsing
        str = in.readLine();
        while (!str.equals("")) {
            String[] h = str.split(": ");
            headers.put(h[0], h[1]);
            str = in.readLine();
        }

        // body parsing
        if (headers.containsKey("Content-Length")) {
            String boundary = headers.get("Content-Type").split("=")[1];
            str = in.readLine();
            while (!str.equals("--" + boundary + "--")) {
                assert boundary.equals(str);
                String key = in.readLine().split("=")[1].replace('"',' ').strip();
                in.readLine();
                String value = in.readLine();
                body.put(key, value);
                str = in.readLine();
            }
        }
    }

    @Override
    public String toString() {
        String s = "";
        s += method + " " + uri + " " + http_ver + '\n';
        for (String h : headers.keySet()) {
            s += h + ": " + headers.get(h) + '\n';
        }
        s += '\n';
        for (String k : body.keySet()) {
            s += k + "=" + body.get(k) + '\n';
        }
        return s;
    }
}
