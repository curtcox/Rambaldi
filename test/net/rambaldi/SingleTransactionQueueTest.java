package net.rambaldi;

import static junit.framework.Assert.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class SingleTransactionQueueTest {

    
    @Test
    public void take_gets_put() {
        SingleTransactionQueue queue = new SingleTransactionQueue();
        assertTrue(queue.isEmpty());
        
        Transaction in = new Transaction();
        queue.put(in);
        assertFalse(queue.isEmpty());
        
        assertEquals(in,queue.take());
        assertTrue(queue.isEmpty());
    }
}
