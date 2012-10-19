package tests.acceptance;

import net.rambaldi.Log.Log;
import net.rambaldi.Log.FakeLog;
import net.rambaldi.process.*;
import net.rambaldi.time.Timestamp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Acceptance tests for launching an external processor.
 * @author Curt
 */
public class External_Process_Test {

    IO io = new SimpleIO();
    FileSystem.Path temp = null;//Paths.get("tempDir");
    StateOnDisk state;
    Log log = new FakeLog();
    FileSystem fileSystem = new SimpleFileSystem(null);

    @Before
    public void Before() throws Exception {
        //io = new DebugIO(io,System.out);
        state = new StateOnDisk(temp,io,fileSystem);
        state.delete();
        state.setRequestProcessor(new EchoProcessor());
        state.persist();
    }

    @After
    public void After() throws Exception {
        state.delete();
    }

    @Test
    public void Read_from_standard_in_and_write_to_standard_out() throws Exception {
        Request request = request();
        TransactionProcessor processor = TransactionProcessors.newExternal(state,io,log);

        Response response = processor.process(request);
        assertEquals(request,response.request);
    }

    @Test
    public void Read_2_requests_from_standard_in_and_write_to_standard_out() throws Exception {
        Request request = request();
        TransactionProcessor processor = TransactionProcessors.newExternal(state,io,log);

        Response response1 = processor.process(request);
        assertEquals(request,response1.request);

        Response response2 = processor.process(request);
        assertEquals(request,response2.request);
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
