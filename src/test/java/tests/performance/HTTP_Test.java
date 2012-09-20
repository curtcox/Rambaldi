package tests.performance;

import net.rambaldi.Log.Log;
import net.rambaldi.Log.SimpleLog;
import net.rambaldi.http.*;
import net.rambaldi.log.FakeLog;
import net.rambaldi.process.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tests.acceptance.PageGetter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

public class HTTP_Test {

    final PageGetter pageGetter = new PageGetter();
    IO io = new SimpleIO();
    Log log = new FakeLog();
    Path temp = Paths.get("tempDir");
    StateOnDisk state;
    FileSystem fileSystem = new SimpleFileSystem();

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
    public void Serve_100_pages_using_an_external_process() throws Exception {
        HttpTransactionsTakesAtMost(2, 10);
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
        TransactionProcessor processor = TransactionProcessors.newExternal(state,io,log);
        HttpTransactionProcessor httpProcessor = new TransactionProcessorAsHttpTransactionProcessor(processor);
        httpProcessor = new DebugHttpTransactionProcessor(httpProcessor,new SimpleLog("HttpTransactionProcessor",System.err));
        HttpConnection.Factory connectionFactory = new SimpleHttpConnectionFactory(port);
        connectionFactory = new DebugHttpConnectionFactory(connectionFactory,new SimpleLog("Connection Factory",System.err));
        Executor executor = Executors.newSingleThreadExecutor();
        Context context = new SimpleContext();
        HttpConnection.Handler handler = new SimpleHttpConnectionHandler(httpProcessor);
        try (SimpleHttpServer server = new SimpleHttpServer(executor,connectionFactory,handler);) {
            server.start();
            for (int i=0; i<max; i++) {
                pageGetter.getPage("http://localhost:" + port);
            }
        }
    }

}
