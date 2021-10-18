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
    public HashMap<String,String> reqParams;
    public ArrayList<String> body;

    Request(BufferedReader in) throws IOException {
        String str;
        headers = new HashMap<String, String>();
        body = new ArrayList<String>();

        str = in.readLine();
        if (str == null)
            return;
        method = str.split(" ")[0];
        uri = str.split(" ")[1];
        http_ver = str.split(" ")[2];

        str = in.readLine();
        while (!str.equals("")) {
            String[] h = str.split(": ");
            headers.put(h[0], h[1]);
            str = in.readLine();
        }

        /*
        if (method.equals("POST")) {
            str = in.readLine();
            while (!str.equals("")) {
                body.add(str);
                str = in.readLine();
            }
        }
        */
    }

    @Override
    public String toString() {
        String s = "";
        s += method + " " + uri + " " + http_ver + '\n';
        for (String h : headers.keySet()) {
            s += h + ": " + headers.get(h) + '\n';
        }
        Iterator<String> i = body.iterator();
        while (i.hasNext()) {
            s += i.next() + '\n';
        }
        return s;
    }
}
