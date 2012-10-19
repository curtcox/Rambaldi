package tests.acceptance;

import net.rambaldi.Log.Log;
import net.rambaldi.Log.SimpleLog;
import net.rambaldi.http.*;
import net.rambaldi.process.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HTTP_Test {

    final URLPageGetter pageGetter = new URLPageGetter();
    IO io = new SimpleIO();
    Log log = new SimpleLog("HTTP Test",System.err);
    FileSystem.Path temp = null;//Paths.get("tempDir");
    StateOnDisk state;
    FileSystem fileSystem = new SimpleFileSystem(null);
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
        try (SimpleHttpServer server = EchoHttpServer.newServer(port)) {
            String page = pageGetter.getPage("http://localhost:" + port);
            assertTrue(page, page.contains("HTTP"));
        }
    }


    @Test
    public void I_should_be_able_to_serve_2_pages_with_an_internal_processor_via_HTTP() throws Exception {
        try (SimpleHttpServer server = EchoHttpServer.newServer(port)) {
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
        HttpConnection.Handler handler = new SimpleHttpConnectionHandler(httpProcessor);
        SimpleHttpServer server = SimpleHttpServer.builder().connections(connectionFactory).handler(handler).build();
        server.start();
        return server;
    }
}
