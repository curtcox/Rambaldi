package net.rambaldi;

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
        
        Transaction in = new Transaction(null){};
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

}
