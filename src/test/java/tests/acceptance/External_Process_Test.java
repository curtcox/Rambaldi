package tests.acceptance;

import net.rambaldi.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

/**
 * Core acceptance tests.
 * @author Curt
 */
public class External_Process_Test {

    final IO io = new SimpleIO();
    StateOnDisk state;

    @Before
    public void Before() {
        state = new StateOnDisk();
        EchoProcessor         echo = new EchoProcessor();
    }

    @After
    public void After() {
        state.delete();
    }

    @Test
    public void Read_from_stdin_and_write_to_stdout() throws InterruptedException {
        SingleTransactionQueue  in = new SingleTransactionQueue(io);
        SingleTransactionQueue out = new SingleTransactionQueue(io);
        OutputStream           err = null;
        Request            request = request();

        Process process = TransactionProcessors.startExternal(in.asInputStream(),out.asOutputStream(),err,state.path);

        in.put(request);
        Thread.sleep(1000);

        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertEquals(request,response.request);

    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
