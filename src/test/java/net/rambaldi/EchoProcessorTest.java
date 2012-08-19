package net.rambaldi;

import static junit.framework.Assert.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class EchoProcessorTest {

    @Test
    public void process_() {
        EchoProcessor echo = new EchoProcessor();
        Request request = new Request(null);
        Context context = null;
        
        Response response = echo.process(request, context);
        
        assertEquals(request,response.request);
    }
}
