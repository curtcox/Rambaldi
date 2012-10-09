package net.rambaldi.process;

import java.io.IOException;
import static org.junit.Assert.*;

import net.rambaldi.time.Timestamp;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class InputStreamFromTransactionSourceTest {
    
    final IO io = new SimpleIO();

    @Test(expected=NullPointerException.class)
    public void constructor_requires_io() {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        new InputStreamFromTransactionSource(queue,null);
    }

    @Test(expected=NullPointerException.class)
    public void constructor_requires_TransactionSource() {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        new InputStreamFromTransactionSource(null,io);
    }

    @Test
    public void read_causes_transaction_to_be_taken_when_no_bytes_in_buffer() throws IOException {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        queue.put(request());
        assertFalse(queue.isEmpty());
        InputStreamFromTransactionSource test = new InputStreamFromTransactionSource(queue,io);
        
        int value = test.read();
        
        assertTrue(queue.isEmpty());
    }

    @Test
    public void read_returns_minus_one_when_no_bytes_available() throws IOException {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        InputStreamFromTransactionSource test = new InputStreamFromTransactionSource(queue,io);
        
        int actual = test.read();
        
        assertEquals(-1,actual);
    }

    @Test
    public void read_returns_bytes_when_there_are_transactions() throws IOException {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        Request request = request();
        queue.put(request);
        InputStreamFromTransactionSource test = new InputStreamFromTransactionSource(queue,io);
        
        assertTrue(test.read()>=0);
    }

    @Test
    public void read_reads_stream_that_contains_enough_bytes_for_transaction() throws IOException {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        Request request = request();
        queue.put(request);
        InputStreamFromTransactionSource test = new InputStreamFromTransactionSource(queue,io);
        
        for (int i=0; i<12; i++) {
            int value = test.read();
            assertTrue(String.format("value[%1s]=%2s",i,value), value>=0);
        }
    }

    @Test
    public void read_reads_bytes_corresponding_to_transaction() throws IOException {
        SingleTransactionQueue queue = new SingleTransactionQueue(io);
        Request expected = request();
        queue.put(expected);
        InputStreamFromTransactionSource test = new InputStreamFromTransactionSource(queue,io);
        
        Transaction actual = io.readTransaction(test);

        assertEquals(expected,actual);        
    }

    private Request request() {
        return new Request("",new Timestamp(33));
    }
  
}
