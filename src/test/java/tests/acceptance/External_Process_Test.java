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
        state.delete();
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
        TransactionProcessor processor = TransactionProcessors.newExternal(state,io);

        Response response = processor.process(request);
        assertEquals(request,response.request);
    }

    @Test
    public void Read_2_requests_from_standard_in_and_write_to_standard_out() throws Exception {
        Request request = request();
        TransactionProcessor processor = TransactionProcessors.newExternal(state,io);

        Response response1 = processor.process(request);
        assertEquals(request,response1.request);

        Response response2 = processor.process(request);
        assertEquals(request,response2.request);
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
