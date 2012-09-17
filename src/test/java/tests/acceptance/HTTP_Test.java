package tests.acceptance;

import net.rambaldi.Log.SimpleLog;
import net.rambaldi.http.*;
import net.rambaldi.process.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HTTP_Test {

    final PageGetter pageGetter = new PageGetter();
    IO io = new SimpleIO();
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
    public void I_should_be_able_to_serve_a_page_with_an_internal_processor_via_HTTP() throws Exception {
        final int port = 4242;
        HttpRequestProcessor httpRequestProcessor = new HttpRequestEchoProcessor();
        Context context = new SimpleContext();
        HttpTransactionProcessor httpProcessor = new SimpleHttpTransactionProcessor(httpRequestProcessor,context);
        httpProcessor = new DebugHttpTransactionProcessor(httpProcessor,new SimpleLog("HttpTransactionProcessor",System.err));
        HttpConnection.Factory connectionFactory = new SimpleHttpConnectionFactory(port);
        connectionFactory = new DebugHttpConnectionFactory(connectionFactory,new SimpleLog("Connection Factory",System.err));
        Executor executor = Executors.newSingleThreadExecutor();
        try (SimpleHttpServer server = new SimpleHttpServer(executor,connectionFactory,httpProcessor);) {
            server.start();
            String page = pageGetter.getPage("http://localhost:" + port);
            assertTrue(page, page.contains("HTTP"));
        }
    }

    @Test
    public void I_should_be_able_to_serve_a_page_with_an_external_processor_via_HTTP() throws Exception {
        final int port = 4242;
        TransactionProcessor processor = TransactionProcessors.newExternal(state,io);
        HttpTransactionProcessor httpProcessor = new TransactionProcessorAsHttpTransactionProcessor(processor);
        httpProcessor = new DebugHttpTransactionProcessor(httpProcessor,new SimpleLog("HttpTransactionProcessor",System.err));
        HttpConnection.Factory connectionFactory = new SimpleHttpConnectionFactory(port);
        connectionFactory = new DebugHttpConnectionFactory(connectionFactory,new SimpleLog("Connection Factory",System.err));
        Executor executor = Executors.newSingleThreadExecutor();
        try (SimpleHttpServer server = new SimpleHttpServer(executor,connectionFactory,httpProcessor);) {
            server.start();
            String page = pageGetter.getPage("http://localhost:" + port);
            assertTrue(page, page.contains("HTTP"));
        }
    }

}
