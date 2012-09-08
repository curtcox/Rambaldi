package net.rambaldi.process;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.*;

import net.rambaldi.process.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class OutputStreamAsTransactionSinkTest {

    final SimpleIO io = new SimpleIO();

    @Test(expected=NullPointerException.class)
    public void constructor_requires_stream() throws IOException {
        new OutputStreamAsTransactionSink(null,io);
    }

    @Test(expected=NullPointerException.class)
    public void constructor_requires_io() throws IOException {
        new OutputStreamAsTransactionSink(new ByteArrayOutputStream(),null);
    }

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
        Transaction result = io.readTransaction(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(result,expected);
    }
   
}
