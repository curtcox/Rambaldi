package tests.performance;

import net.rambaldi.http.HttpConnection;
import net.rambaldi.http.HttpRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * For getting a page from a URL.
 */
public final class SimplePageGetter {

    final int timeout = 10 * 1000;
    final BufferedReader in;
    final OutputStream out;
    final byte[] requestBytes;

    public SimplePageGetter(HttpRequest request, HttpConnection connection) {
        in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));
        out = connection.getOutputStream();
        requestBytes = (request.toString() + "\r\n").getBytes();
    }

    public String getPage() throws IOException {
        out.write(requestBytes);

        StringWriter out = new StringWriter();
        for (String line = in.readLine(); line!= null; line = in.readLine()) {
            out.write(line);
            if (line.contains("</html>")) {
                break;
            }
        }

        return out.toString();
    }

}
