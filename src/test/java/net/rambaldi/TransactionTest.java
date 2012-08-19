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
    public void uses_Timestamp_from_contructor() {
        Timestamp timestamp = new Timestamp(1);
        Transaction transaction = new Transaction(null,timestamp);
        
        assertSame(timestamp,transaction.timestamp);
    }

    @Test
    public void uses_bytes_from_contructor() {

        byte[] expected = "".getBytes();
        Transaction transaction = new Transaction(expected,null);
        byte[] actual = transaction.bytes;

        assertSame(expected,actual);
    }

    @Test
    public void is_serializable() throws Exception {
        Transaction transaction = new Transaction(new byte[0],new Timestamp(1));
        Transaction copy = Copier.copy(transaction);
        
        assertEquals(transaction,copy);
    }

}
