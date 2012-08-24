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
public class InputTransactionSourceTest {

    @Test
    public void take_request() throws IOException {
        Request expected = new Request("",new Timestamp(0));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Data.write(out, expected);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        InputTransactionSource source = new InputTransactionSource(in);
        
        Transaction result = source.take();
        
        assertEquals(expected,result);
    }
}
