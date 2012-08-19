package net.rambaldi;
import static org.junit.Assert.*;
import org.junit.Test;
import tests.acceptance.Copier;
/**
 *
 * @author Curt
 */
public class TransactionTest {

    @Test
    public void usesTimestampFromContructor() {
        Timestamp timestamp = new Timestamp(1);
        Transaction transaction = new Transaction(timestamp);
        
        assertSame(timestamp,transaction.timestamp);
    }

    @Test
    public void is_serializable() throws Exception {
        Transaction transaction = new Transaction(new Timestamp(1));
        Transaction copy = Copier.copy(transaction);
        
        assertEquals(transaction,copy);
    }

}
