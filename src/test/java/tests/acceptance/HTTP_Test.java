package tests.acceptance;

import net.rambaldi.*;
import net.rambaldi.http.HTTPEchoProcessor;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HTTP_Test {

    final IO io = new SimpleIO();

    @Test
    public void I_should_be_able_to_serve_a_page_via_HTTP() throws Exception {

        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                ServerSocket   serverSocket = new ServerSocket(4242);
                Socket               socket = serverSocket.accept();
                HTTPEchoProcessor      echo = new HTTPEchoProcessor();
                ResponseProcessor responses = new SimpleResponseProcessor();
                Context             context = new SimpleContext();
                StreamTransactionProcessor system = new StreamTransactionProcessor(socket.getInputStream(),socket.getOutputStream(),System.err,io,context,echo,responses);
                system.process();
                return null;
            }
        };

        Executors.newSingleThreadExecutor().submit(callable);
        String page = getPage("http://localhost:4242");
        assertTrue(page.contains("HTTP"));
    }

    String getPage(String page) throws IOException {
        URL server = new URL(page);
        System.out.println("open connection");
        URLConnection connection = server.openConnection();
        System.out.println("timeout = " + connection.getConnectTimeout());
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
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
