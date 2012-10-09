package net.rambaldi.process;

import java.io.IOException;
import static junit.framework.Assert.*;

import net.rambaldi.time.Timestamp;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class OutputStreamToTransactionSinkTest {

    final IO io = new SimpleIO();

    @Test(expected=NullPointerException.class)
    public void contructor_requires_sink() {
       new OutputStreamToTransactionSink(null,null);
    }

    @Test
    public void isTransactionComplete_returns_false_when_nothing_written() {
       SingleTransactionQueue queue = new SingleTransactionQueue(io);
       OutputStreamToTransactionSink out = new OutputStreamToTransactionSink(queue,io);
       assertFalse(out.isTransactionComplete());
    }
    
    @Test
    public void isTransactionComplete_returns_true_when_transaction_written() throws IOException {
       SingleTransactionQueue queue = new SingleTransactionQueue(io);
       OutputStreamToTransactionSink out = new OutputStreamToTransactionSink(queue,io);
       io.write(out, request());
       assertTrue(out.isTransactionComplete());
    }

    @Test
    public void isTransactionComplete_puts_transaction_to_sink() throws IOException {
       SingleTransactionQueue queue = new SingleTransactionQueue(io);
       OutputStreamToTransactionSink out = new OutputStreamToTransactionSink(queue,io);
       Request expected = request();
       
       io.write(out, expected);
       
       assertTrue(out.isTransactionComplete());
       Transaction actual = queue.take();
       assertEquals(expected,actual);
    }

    Request request() {
        return new Request("",new Timestamp(0));
    }
}
