package net.rambaldi;

import java.io.OutputStream;
import static junit.framework.Assert.*;
import org.junit.Test;
import tests.acceptance.Copier;

/**
 *
 * @author Curt
 */
public class SingleTransactionQueueTest {

    
    @Test
    public void take_gets_put() {
        SingleTransactionQueue queue = new SingleTransactionQueue();
        assertTrue(queue.isEmpty());
        
        Transaction in = transaction();
        queue.put(in);
        assertFalse(queue.isEmpty());
        
        assertEquals(in,queue.take());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void is_serializable() throws Exception {
        SingleTransactionQueue queue = new SingleTransactionQueue();
        SingleTransactionQueue copy = Copier.copy(queue);
        assertTrue(copy instanceof SingleTransactionQueue);
    }

    @Test
    public void asInputStream_returns_stream_where_transactions_can_be_read_from() {
        SingleTransactionQueue queue = new SingleTransactionQueue();
        Transaction transaction = transaction();
        queue.put(transaction);
        
        InputTransactionSource in = new InputTransactionSource(queue.asInputStream());
        
        assertEquals(transaction,in.take());
    }

    @Test
    public void asOutputStream_returns_stream_where_transaction_can_be_written_to() {
        SingleTransactionQueue queue = new SingleTransactionQueue();
        Transaction transaction = transaction();
        
        final OutputStream OutputStream = queue.asOutputStream();
        
        OutputTransactionSink out = new OutputTransactionSink(OutputStream);
        out.put(transaction);
        assertFalse(queue.isEmpty());
        assertEquals(transaction,queue.take());
    }

    private Transaction transaction() {
        return new Request("",new Timestamp(0));
    }
}
