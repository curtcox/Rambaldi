package tests.acceptance;

import net.rambaldi.*;
import net.rambaldi.IO;
import net.rambaldi.SimpleIO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Acceptance tests for launching an external processor.
 * @author Curt
 */
public class External_Process_Test {

    final IO io = new SimpleIO();
    StateOnDisk state;

    @Before
    public void Before() throws Exception {
        state = new StateOnDisk(Paths.get("tempDir"),io);
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
        TransactionSink sink = new OutputStreamAsTransactionSink(server.getInput(),io);
        TransactionSource source = new InputStreamAsTransactionSource(server.getOutput(),io);

        assertFalse(server.isUp());
        server.start();
        assertTrue(server.isUp());

        sink.put(request);
        Thread.sleep(1000);

        Response response = (Response) source.take();
        assertEquals(request,response.request);

        server.stop();
        assertFalse(server.isUp());
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
