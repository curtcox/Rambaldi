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
public class InputStreamAsTransactionSourceTest {

    final SimpleIO io = new SimpleIO();
    
    @Test
    public void take_request() throws IOException {
        Request expected = new Request("",new Timestamp(0));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        io.write(out, expected);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        InputStreamAsTransactionSource source = new InputStreamAsTransactionSource(in,io);
        
        Transaction result = source.take();
        
        assertEquals(expected,result);
    }
}
