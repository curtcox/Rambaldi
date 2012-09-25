package tests.acceptance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * For getting a page from a URL.  It has a few problems for performance testing:
 * <ul>
 *     <li> It will throw an exception when used under heavy sustained load</li>
 *         <pre>
 *             java.net.SocketException: No buffer space available (maximum connections reached?): connect
 *         </pre>
 *     <li> It isn't particularly fast.</li>
 * </ul>
 */
public final class URLPageGetter {

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
