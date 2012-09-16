package tests.acceptance;

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
        io = new DebugIO(io,System.out);
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
    public void I_should_be_able_to_serve_a_page_via_HTTP() throws Exception {
        final int port = 4242;
        TransactionProcessor processor = TransactionProcessors.newExternal(state,io);
        HttpTransactionProcessor httpProcessor = new TransactionProcessorAsHttpTransactionProcessor(processor);
        Executor executor = Executors.newSingleThreadExecutor();
        SimpleHttpServer server = new SimpleHttpServer(executor,httpProcessor,port);
        System.out.println("starting");
        server.start();
        System.out.println("processor started");

        String page = pageGetter.getPage("http://localhost:" + port);
        assertTrue(page,page.contains("HTTP"));
    }


}
