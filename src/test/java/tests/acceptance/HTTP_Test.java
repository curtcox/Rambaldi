package tests.acceptance;

import net.rambaldi.Log.Log;
import net.rambaldi.Log.SimpleLog;
import net.rambaldi.http.*;
import net.rambaldi.log.FakeLog;
import net.rambaldi.process.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
    Log log = new FakeLog();
    Path temp = Paths.get("tempDir");
    StateOnDisk state;
    FileSystem fileSystem = new SimpleFileSystem();
    final int port = 4242;

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
        try (SimpleHttpServer server = newServer()) {
            String page = pageGetter.getPage("http://localhost:" + port);
            assertTrue(page, page.contains("HTTP"));
        }
    }

    SimpleHttpServer newServer() throws IOException {
        HttpRequestProcessor httpRequestProcessor = new HttpRequestEchoProcessor();
        Context context = new SimpleContext();
        HttpTransactionProcessor httpProcessor = new SimpleHttpTransactionProcessor(httpRequestProcessor,context);
        httpProcessor = new DebugHttpTransactionProcessor(httpProcessor,new SimpleLog("HttpTransactionProcessor",System.err));
        HttpConnection.Factory connectionFactory = new SimpleHttpConnectionFactory(port);
        connectionFactory = new DebugHttpConnectionFactory(connectionFactory,new SimpleLog("Connection Factory",System.err));
        Executor executor = Executors.newSingleThreadExecutor();
        SimpleHttpServer server = new SimpleHttpServer(executor,connectionFactory,httpProcessor);
        server.start();
        return server;
    }

    @Test
    public void I_should_be_able_to_serve_2_pages_with_an_internal_processor_via_HTTP() throws Exception {
        try (SimpleHttpServer server = newServer()) {
            String page1 = pageGetter.getPage("http://localhost:" + port);
            assertTrue(page1, page1.contains("HTTP"));
            String page2 = pageGetter.getPage("http://localhost:" + port);
            assertTrue(page2, page2.contains("HTTP"));
        }
    }

    @Test
    public void I_should_be_able_to_serve_a_page_with_an_external_processor_via_HTTP() throws Exception {
        try (SimpleHttpServer server = newExternalServer()) {
            String page = pageGetter.getPage("http://localhost:" + port);
            assertTrue(page, page.contains("HTTP"));
        }
    }

    SimpleHttpServer newExternalServer() throws ProcessCreationException, IOException {
        TransactionProcessor processor = TransactionProcessors.newExternal(state,io,log);
        HttpTransactionProcessor httpProcessor = new TransactionProcessorAsHttpTransactionProcessor(processor);
        httpProcessor = new DebugHttpTransactionProcessor(httpProcessor,new SimpleLog("HttpTransactionProcessor",System.err));
        HttpConnection.Factory connectionFactory = new SimpleHttpConnectionFactory(port);
        connectionFactory = new DebugHttpConnectionFactory(connectionFactory,new SimpleLog("Connection Factory",System.err));
        Executor executor = Executors.newSingleThreadExecutor();
        SimpleHttpServer server = new SimpleHttpServer(executor,connectionFactory,httpProcessor);
        server.start();
        return server;
    }
}
