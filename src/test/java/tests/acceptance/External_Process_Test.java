package tests.acceptance;

import net.rambaldi.process.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.*;

/**
 * Acceptance tests for launching an external processor.
 * @author Curt
 */
public class External_Process_Test {

    IO io = new SimpleIO();
    Path temp = Paths.get("tempDir");
    StateOnDisk state;
    FileSystem fileSystem = new SimpleFileSystem();

    @Before
    public void Before() throws Exception {
        //io = new DebugIO(io,System.out);
        state = new StateOnDisk(temp,io,fileSystem);
        state.setProcessor(new EchoProcessor());
        state.persist();
    }

    @After
    public void After() throws Exception {
        state.delete();
    }

    @Test
    public void Read_from_standard_in_and_write_to_standard_out() throws Exception {
        Request request = request();

        StreamServer server = TransactionProcessors.newExternal(state);
        assertFalse(server.isUp());

        server.start();
        assertTrue(server.isUp());

        TransactionSink sink = new OutputStreamAsTransactionSink(server.getInput(),io);
        TransactionSource source = new InputStreamAsTransactionSource(server.getOutput(),io);

        sink.put(request);
        server.getInput().flush();
        Thread.sleep(1000);

        Response response = (Response) source.take();
        assertEquals(request,response.request);

        server.stop();
        assertFalse(server.isUp());
    }

    @Test
    public void Read_2_requests_from_standard_in_and_write_to_standard_out() throws Exception {
        final Request request = request();

        final StreamServer server = TransactionProcessors.newExternal(state);
        assertFalse(server.isUp());

        server.start();
        assertTrue(server.isUp());

        final TransactionSink sink = new OutputStreamAsTransactionSink(server.getInput(),io);
        final TransactionSource source = new InputStreamAsTransactionSource(server.getOutput(),io);

        FutureTask<Integer> requests = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sink.put(request);
                sink.put(request);
                server.getInput().flush();
                return 2;
            }
        });

        FutureTask<Integer> responses = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Response response = (Response) source.take();
                assertEquals(request,response.request);
                response = (Response) source.take();
                assertEquals(request,response.request);
                return 2;
            }
        });

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(requests);
        executorService.submit(responses);
        executorService.shutdown();
        assertEquals(2, (int) requests.get());
        assertEquals(2,(int)responses.get());
        server.stop();
        assertFalse(server.isUp());
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
