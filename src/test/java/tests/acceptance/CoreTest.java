package tests.acceptance;

import java.io.IOException;
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
        Request            request = new Request("",null);
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
        Timestamp t1 = new Timestamp(1);
        Timestamp t2 = new Timestamp(2);
        SingleTransactionQueue  in = new SingleTransactionQueue(); 
        SingleTransactionQueue out = new SingleTransactionQueue(); 
        SingleTransactionQueue err = null;
        TimestampProcessor stamper = new TimestampProcessor();
        Context            context = new SimpleContext();
        Request           request1 = new Request("",t1);
        SimpleTransactionProcessor processor1 = new SimpleTransactionProcessor(in,out,err,context,stamper,null);
        
        in.put(request1);
        processor1.process();

        assertFalse(out.isEmpty());
        TimestampResponse response1 = (TimestampResponse) out.take();
        assertSame(request1,response1.request);
        assertSame(t1,response1.timestamp);
        assertNull(null,response1.previousRequest);
        
        SimpleTransactionProcessor processor2 = new SimpleTransactionProcessor(in,out,err,context,stamper,null);
        
        Request request2 = new Request("",t2);
        in.put(request2);
        processor2.process();
        assertFalse(out.isEmpty());
        TimestampResponse response2 = (TimestampResponse) out.take();
        assertSame(request2,response2.request);
        assertSame(t2,response2.timestamp);
        assertSame(t1,response2.previousRequest);        
    }

    /**
     *I should be able to persist state between requests
     */
    @Test
    public void Persist_state_between_requests() throws Exception {
        Timestamp t1 = new Timestamp(1);
        Timestamp t2 = new Timestamp(2);
        SingleTransactionQueue  in = new SingleTransactionQueue(); 
        SingleTransactionQueue out = new SingleTransactionQueue(); 
        SingleTransactionQueue err = null;
        TimestampProcessor stamper = new TimestampProcessor();
        Context            context = new SimpleContext();
        Request           request1 = new Request("",t1);
        SimpleTransactionProcessor processor = new SimpleTransactionProcessor(in,out,err,context,stamper,null);
        
        in.put(request1);
        processor.process();
        
        assertFalse(out.isEmpty());
        TimestampResponse response1 = (TimestampResponse) out.take();
        assertSame(request1,response1.request);
        assertSame(t1,response1.timestamp);
        assertNull(null,response1.previousRequest);
        
        processor = Copier.copy(processor);
        in = (SingleTransactionQueue) processor.in;
        out = (SingleTransactionQueue) processor.out;
        
        Request request2 = new Request("",t2);
        in.put(request2);
        processor.process();
        assertFalse(out.isEmpty());
        TimestampResponse response2 = (TimestampResponse) out.take();
        assertEquals(request2,response2.request);
        assertEquals(t2,response2.timestamp);
        assertEquals(t1,response2.previousRequest);        
    }
    
    /**
     * I should be able to handle responses asynchronously from requests
     */
    @Test
    public void Handle_responses_asynchronously_from_requests() {
//        fail();
    }
    
}
