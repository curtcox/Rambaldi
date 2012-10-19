package tests.performance;

import net.rambaldi.Log.Log;
import net.rambaldi.Log.SimpleLog;
import net.rambaldi.file.FileSystem;
import net.rambaldi.file.SimpleFileSystem;
import net.rambaldi.http.*;
import net.rambaldi.process.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static net.rambaldi.http.HttpRequest.Connection.keep_alive;
import static org.junit.Assert.assertTrue;

public class HTTP_Performance_Test {

    IO io = new SimpleIO();
    Log log = new SimpleLog("HTTP Test",System.err);
    FileSystem.RelativePath temp = null;//Paths.get("tempDir");
    StateOnDisk state;
    FileSystem fileSystem = new SimpleFileSystem(null);

    @Before
    public void Before() throws Exception {
        io = new DebugIO(io,new SimpleLog("Client IO",System.err));
        state = new StateOnDisk(temp,io,fileSystem);
        state.delete();
        HttpRequestProcessor httpEchoProcessor = new HttpRequestEchoProcessor();
        RequestProcessor requestProcessor = new HttpRequestProcessorAsRequestProcessor(httpEchoProcessor);
        state.setRequestProcessor(requestProcessor);
        state.persist();
    }

    @After
    public void After() throws Exception {
        state.delete();
    }

    @Test
    public void Serve_000_100_pages_using_HTTP_over_a_socket() throws Exception {
        HttpTransactionsTakesAtMost(100, 5);
    }

    @Test
    public void Serve_010_000_pages_using_HTTP_over_a_socket() throws Exception {
        HttpTransactionsTakesAtMost(10* 1000, 5);
    }

    @Test
    public void Serve_100_000_pages_using_HTTP_over_a_socket() throws Exception {
        HttpTransactionsTakesAtMost(100 * 1000, 10);
    }

    private void HttpTransactionsTakesAtMost(int max, int allowedSeconds) throws Exception {
        long start = System.currentTimeMillis();
        httpTransactions(max);
        long end = System.currentTimeMillis();
        long durationSeconds = (end - start) / 1000;
        assertTrue(max + " should finish in " + allowedSeconds + " but took " + durationSeconds,durationSeconds <= allowedSeconds);
    }

    void httpTransactions(int max) throws Exception {
        final int port = 4242;
        HttpRequest request = HttpRequest.builder().connection(keep_alive).build();
        try (SimpleHttpServer server = EchoHttpServer.newServer(port); HttpConnection connection = newConnection(port)) {
            final SimplePageGetter pageGetter = new SimplePageGetter(request,connection);
            for (int i=0; i<max; i++) {
                pageGetter.getPage();
            }
        }
    }

    private HttpConnection newConnection(int port) throws IOException {
        Socket socket = new Socket();
        int timeout = 1000;
        SocketAddress address = new InetSocketAddress("localhost", port);
        socket.connect(address,timeout);
        return new SimpleHttpConnection(socket.getInputStream(),socket.getOutputStream());
    }

}
