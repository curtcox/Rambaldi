package tests.acceptance;

import net.rambaldi.http.HttpEchoProcessor;
import net.rambaldi.http.HttpRequestReader;
import net.rambaldi.http.HttpResponseWriter;
import net.rambaldi.http.SimpleHttpServer;
import net.rambaldi.process.*;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HTTP_Test {

    final PageGetter pageGetter = new PageGetter();

    @Test
    public void I_should_be_able_to_serve_a_page_via_HTTP() throws Exception {
        final int port = 4242;
        HttpEchoProcessor echo = new HttpEchoProcessor();
        SimpleHttpServer server = new SimpleHttpServer(echo,port);
        server.start();

        String page = pageGetter.getPage("http://localhost:" + port);
        assertTrue(page.contains("HTTP"));
    }


}
