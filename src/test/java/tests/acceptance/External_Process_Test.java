package tests.acceptance;

import net.rambaldi.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * Acceptance tests for launching an external processor.
 * @author Curt
 */
public class External_Process_Test {

    final IO io = new SimpleIO();
    StateOnDisk state;

    @Before
    public void Before() {
        state = new StateOnDisk();
        state.setProcessor(new EchoProcessor());
        state.persist();
    }

    @After
    public void After() {
        state.delete();
    }

    @Test
    public void Read_from_standard_in_and_write_to_standard_out() throws Exception {
        SingleTransactionQueue  in = new SingleTransactionQueue(io);
        SingleTransactionQueue out = new SingleTransactionQueue(io);
        OutputStream           err = null;
        Request            request = request();

        StreamServer server = TransactionProcessors.newExternal(in.asInputStream(), out.asOutputStream(), err, state);

        assertFalse(server.isUp());
        server.start();
        assertTrue(server.isUp());

        in.put(request);
        Thread.sleep(1000);

        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertEquals(request,response.request);

        server.stop();
        assertFalse(server.isUp());
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
