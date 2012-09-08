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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Performance_Test {

    IO io = new SimpleIO();
    Path temp = Paths.get("tempDir");
    StateOnDisk state;
    FileSystem fileSystem = new SimpleFileSystem();

    @Before
    public void Before() throws Exception {
        state = new StateOnDisk(temp,io,fileSystem);
        state.setProcessor(new EchoProcessor());
        state.persist();
    }

    @After
    public void After() throws Exception {
        state.delete();
    }

    @Test
    public void Read_100_requests_from_standard_in_and_write_to_standard_out() throws Exception {
        readAndWriteRequestsTakesAtMost(100,1);
    }

    @Test
    public void Read_1_000_requests_from_standard_in_and_write_to_standard_out() throws Exception {
        readAndWriteRequestsTakesAtMost(1000, 10);
    }

    @Test
    public void Read_10_000_requests_from_standard_in_and_write_to_standard_out() throws Exception {
        readAndWriteRequestsTakesAtMost(10000,10);
    }

    @Test
    public void Read_100_000_requests_from_standard_in_and_write_to_standard_out() throws Exception {
        readAndWriteRequestsTakesAtMost(100000, 40);
    }

    private void readAndWriteRequestsTakesAtMost(int max, int allowedSeconds) throws Exception {
        long start = System.currentTimeMillis();
        readAndWriteRequests(max);
        long end = System.currentTimeMillis();
        long durationSeconds = (end - start) / 1000;
        assertTrue(max + " should finish in " + allowedSeconds + " but took " + durationSeconds,durationSeconds <= allowedSeconds);
    }

    private void readAndWriteRequests(final int max) throws Exception {
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
                for (int i=0; i<max; i++) {
                    sink.put(request);
                }
                server.getInput().flush();
                return max;
            }
        });

        FutureTask<Integer> responses = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (int i=0; i<max; i++) {
                    Response response = (Response) source.take();
                    assertEquals(request, response.request);
                }
                return max;
            }
        });

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(requests);
        executorService.submit(responses);

        executorService.shutdown();

        assertEquals(max,(int)requests.get());
        assertEquals(max,(int)responses.get());

        server.stop();
        assertFalse(server.isUp());
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
