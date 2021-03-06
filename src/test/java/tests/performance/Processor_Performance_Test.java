package tests.performance;

import net.rambaldi.Log.Log;
import net.rambaldi.Log.FakeLog;
import net.rambaldi.file.FileSystem;
import net.rambaldi.file.SimpleFileSystem;
import net.rambaldi.file.SimpleRelativePath;
import net.rambaldi.process.*;
import net.rambaldi.time.Timestamp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Processor_Performance_Test {

    IO io = new SimpleIO();
    FileSystem.RelativePath temp = new SimpleRelativePath("tempDir");
    StateOnDisk state;
    FileSystem fileSystem = new SimpleFileSystem(temp);
    Log log = new FakeLog();

    @Before
    public void Before() throws Exception {
        state = new StateOnDisk(temp,io,fileSystem);
        state.setRequestProcessor(new EchoProcessor());
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
        readAndWriteRequestsTakesAtMost(10* 1000,10);
    }

    @Test
    public void Read_100_000_requests_from_standard_in_and_write_to_standard_out() throws Exception {
        readAndWriteRequestsTakesAtMost(100 * 1000, 40);
    }

    private void readAndWriteRequestsTakesAtMost(int max, int allowedSeconds) throws Exception {
        long start = System.currentTimeMillis();
        readAndWriteRequests(max);
        long end = System.currentTimeMillis();
        long durationSeconds = (end - start) / 1000;
        assertTrue(max + " should finish in " + allowedSeconds + " but took " + durationSeconds,durationSeconds <= allowedSeconds);
    }

    private void readAndWriteRequests(int max) throws Exception {
        final Request request = request();

        TransactionProcessor processor = TransactionProcessors.newExternal(state,io,log);
        for (int i=0; i<max; i++) {
            Response response = processor.process(request);
            assertEquals(request, response.request);
        }
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
