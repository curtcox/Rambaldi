package tests.acceptance;

import java.io.OutputStream;
import net.rambaldi.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Core acceptance tests.
 * @author Curt
 */
public class CoreTest {

    @Test
    public void Read_from_queue_and_write_to_queue() {
        SingleTransactionQueue  in = new SingleTransactionQueue(); 
        SingleTransactionQueue out = new SingleTransactionQueue(); 
        SingleTransactionQueue err = null;
        EchoProcessor         echo = new EchoProcessor();
        Context            context = null;
        Request            request = request();
        SimpleTransactionProcessor system = new SimpleTransactionProcessor(in,out,err,context,echo,null);
        
        in.put(request);
        system.process();

        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertSame(request,response.request);
    }

    @Test
    public void Read_from_stdin_and_write_to_stdout() {
        SingleTransactionQueue  in = new SingleTransactionQueue(); 
        SingleTransactionQueue out = new SingleTransactionQueue(); 
        OutputStream           err = null;
        EchoProcessor echo = new EchoProcessor();
        Context    context = null;
        Request    request = request();
        StreamTransactionProcessor system = new StreamTransactionProcessor(in.asInputStream(),out.asOutputStream(),err,context,echo,null);
        
        in.put(request);
        system.process();

        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertSame(request,response.request);
    }

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
        assertSame(t1,response1.getTimestamp());
        assertNull(null,response1.previousRequest);
        
        SimpleTransactionProcessor processor2 = new SimpleTransactionProcessor(in,out,err,context,stamper,null);
        
        Request request2 = new Request("",t2);
        in.put(request2);
        processor2.process();
        assertFalse(out.isEmpty());
        TimestampResponse response2 = (TimestampResponse) out.take();
        assertSame(request2,response2.request);
        assertSame(t2,response2.getTimestamp());
        assertSame(t1,response2.previousRequest);        
    }

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
        assertSame(t1,response1.getTimestamp());
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
        assertEquals(t2,response2.getTimestamp());
        assertEquals(t1,response2.previousRequest);        
    }
    
    @Test
    public void Handle_responses_asynchronously_from_requests() {
//        fail();
    }

    @Test
    public void In_dev_I_should_be_able_to_hot_deploy_program_changes() {
//        fail();
    }

    @Test
    public void I_should_be_able_to_update_the_program_via_an_update_transaction() {
//        fail();
    }

    @Test
    public void I_should_be_able_to_migrate_state_via_an_update_transaction() {
//        fail();
    }

    @Test
    public void I_should_be_able_to_persist_to_disk_via_a_transaction() {
//        fail();
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
