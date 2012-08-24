package net.rambaldi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class OutputTransactionSinkTest {

    @Test
    public void put_empty_request() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputTransactionSink sink = new OutputTransactionSink(out);
        Transaction expected = new Request("",new Timestamp(0));
        
        sink.put(expected);
        
        out.close();
        Transaction result = (Transaction) Data.readTransaction(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(result,expected);
    }
}
