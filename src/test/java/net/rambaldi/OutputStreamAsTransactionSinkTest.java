package net.rambaldi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.Test;
import tests.acceptance.Copier;

/**
 *
 * @author Curt
 */
public class OutputStreamAsTransactionSinkTest {

    final SimpleIO io = new SimpleIO();

    @Test(expected=NullPointerException.class)
    public void put_rejects_null_transactions() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamAsTransactionSink sink = new OutputStreamAsTransactionSink(out,io);
        
        sink.put(null);
    }

    @Test
    public void put_empty_request() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamAsTransactionSink sink = new OutputStreamAsTransactionSink(out,io);
        Transaction expected = new Request("",new Timestamp(0));
        
        sink.put(expected);
        
        out.close();
        Transaction result = (Transaction) io.readTransaction(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(result,expected);
    }
   
}
