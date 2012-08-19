package net.rambaldi;
import static org.junit.Assert.*;
import org.junit.Test;
/**
 *
 * @author Curt
 */
public class TransactionTest {

    @Test
    public void usesTimestampFromContructor() {
        Timestamp timestamp = new Timestamp(1);
        Transaction transaction = new Transaction(timestamp) {};
        
        assertSame(timestamp,transaction.timestamp);
    }
}
