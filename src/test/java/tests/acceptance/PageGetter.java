package tests.acceptance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

public class PageGetter {

    final int timeout = 10 * 1000;

    String getPage(String page) throws IOException {
        URL server = new URL(page);
        System.out.println("open connection");
        URLConnection connection = server.openConnection();
        System.out.println("timeout = " + connection.getConnectTimeout());
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        System.out.println("timeout = " + connection.getConnectTimeout());
        System.out.println("reading");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));
        StringWriter out = new StringWriter();
        for (String line = in.readLine(); line!= null; line = in.readLine()) {
            out.write(line);
        }
        System.out.println("closing");
        in.close();
        System.out.println("closed");

        return out.toString();
    }

}
