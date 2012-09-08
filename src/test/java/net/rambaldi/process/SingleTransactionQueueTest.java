package net.rambaldi.process;

import java.io.InputStream;
import java.io.OutputStream;
import static junit.framework.Assert.*;

import net.rambaldi.process.*;
import org.junit.Test;
import tests.acceptance.Copier;

/**
 *
 * @author Curt
 */
public class SingleTransactionQueueTest {

    final IO io = new SimpleIO();

    @Test
    public void isEmpty_initially_returns_true() {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        assertTrue(queue.isEmpty());       
    }

    @Test(expected=NullPointerException.class)
    public void put_rejects_null_transactions() {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        queue.put(null);       
    }

    @Test
    public void take_gets_put() {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        assertTrue(queue.isEmpty());
        
        Transaction in = transaction();
        queue.put(in);
        assertFalse(queue.isEmpty());
        
        assertEquals(in,queue.take());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void is_serializable() throws Exception {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        SingleTransactionQueue copy = Copier.copy(queue);
        assertTrue(copy instanceof SingleTransactionQueue);
    }

    @Test
    public void asInputStream_returns_stream_where_transactions_can_be_read_from() {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        Transaction transaction = transaction();
        queue.put(transaction);
        
        InputStream inputStream = queue.asInputStream();
        
        InputStreamAsTransactionSource in = new InputStreamAsTransactionSource(inputStream,io);
        assertEquals(transaction,in.take());
    }

    @Test
    public void asOutputStream_returns_stream_where_transaction_can_be_written_to() {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        Transaction transaction = transaction();
        
        final OutputStream out = queue.asOutputStream();     
        OutputStreamAsTransactionSink sink = new OutputStreamAsTransactionSink(out,io);
        sink.put(transaction);
        
        assertFalse(queue.isEmpty());
        assertEquals(transaction,queue.take());
    }

    private Transaction transaction() {
        return new Request("",new Timestamp(0));
    }
}
