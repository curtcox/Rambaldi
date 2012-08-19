package net.rambaldi;

import static junit.framework.Assert.*;
import org.junit.Test;
import tests.acceptance.Copier;

/**
 *
 * @author Curt
 */
public class EchoProcessorTest {

    @Test
    public void process_echos_request() {
        EchoProcessor echo = new EchoProcessor();
        Request request = new Request("",null);
        Context context = null;
        
        Response response = echo.process(request, context);
        
        assertEquals(request,response.request);
    }
    
    @Test
    public void is_serializable() throws Exception {
        EchoProcessor echo = new EchoProcessor();
        EchoProcessor copy = Copier.copy(echo);
        assertTrue(copy instanceof EchoProcessor);
    }
}
