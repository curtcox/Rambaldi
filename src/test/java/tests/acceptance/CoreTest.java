package tests.acceptance;

import net.rambaldi.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Core acceptance tests.
 * @author Curt
 */
public class CoreTest {

    /**
     * I should be able to respond to requests on stdin with responses on stdout
     */
    @Test
    public void Read_from_stdin_and_write_to_stdout() {
        SingleTransactionQueue  in = new SingleTransactionQueue(); 
        SingleTransactionQueue out = new SingleTransactionQueue(); 
        SingleTransactionQueue err = null;
        EchoProcessor         echo = new EchoProcessor();
        Context            context = null;
        Request            request = new Request();
        SimpleTransactionProcessor system = new SimpleTransactionProcessor(in,out,err,context,echo,null);
        
        in.put(request);
        system.process();

        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertSame(request,response.request);
    }

    /**
     *I should be able to save state between requests
     */
    @Test
    public void Save_state_between_requests() {
//        fail();
    }

    /**
     * I should be able to handle responses asynchronously from requests
     */
    @Test
    public void Handle_responses_asynchronously_from_requests() {
//        fail();
    }
    
}
