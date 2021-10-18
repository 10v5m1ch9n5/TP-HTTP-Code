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
    public ArrayList<String> headers;
    public HashMap<String,String> reqParams;
    public ArrayList<String> body;

    Request(BufferedReader in) throws IOException {
        String str;
        headers = new ArrayList<String>();
        body = new ArrayList<String>();

        str = in.readLine();
        method = str.split(" ")[0];
        uri = str.split(" ")[1];
        http_ver = str.split(" ")[2];

        str = in.readLine();
        while (!str.equals("")) {
            headers.add(str);
            str = in.readLine();
        }
/*
        str = in.readLine();
        while (!str.equals("")) {
            body.add(str);
            str = in.readLine();
        }
 */
    }

    @Override
    public String toString() {
        String s = "";
        s += method + " " + uri + " " + http_ver + '\n';
        Iterator<String> i = headers.iterator();
        while (i.hasNext()) {
            s += i.next() + '\n';
        }
        i = body.iterator();
        while (i.hasNext()) {
            s += i.next() + '\n';
        }
        return s;
    }
}
