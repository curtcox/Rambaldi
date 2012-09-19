package tests.acceptance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public final class PageGetter {

    final int timeout = 10 * 1000;

    public String getPage(String page) throws IOException {
        URL server = new URL(page);
        HttpURLConnection connection = (HttpURLConnection) server.openConnection();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));
        StringWriter out = new StringWriter();
        for (String line = in.readLine(); line!= null; line = in.readLine()) {
            out.write(line);
        }
        in.close();

        return out.toString();
    }

}
