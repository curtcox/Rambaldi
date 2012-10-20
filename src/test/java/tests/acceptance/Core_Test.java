package tests.acceptance;

import java.io.PrintStream;

import static org.junit.Assert.*;

import net.rambaldi.process.IO;
import net.rambaldi.process.*;
import net.rambaldi.time.Timestamp;
import org.junit.Test;

/**
 * Core acceptance tests.
 * @author Curt
 */
public class Core_Test {

    final IO io = new SimpleIO();
    
    @Test
    public void Read_from_queue_and_write_to_queue() throws Exception {
        
        SingleTransactionQueue  in = new SingleTransactionQueue(io);
        SingleTransactionQueue out = new SingleTransactionQueue(io); 
        PrintStream            err = null;
        EchoProcessor         echo = new EchoProcessor();
        Context            context = null;
        Request            request = request();
        SimpleTransactionProcessor system = new SimpleTransactionProcessor(in,out,err,context,echo,null);
        
        in.put(request);
        system.call();

        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertSame(request, response.request);
    }

    @Test
    public void Read_from_stream_and_write_to_stream() throws Exception {
        SingleTransactionQueue  in = new SingleTransactionQueue(io); 
        SingleTransactionQueue out = new SingleTransactionQueue(io);
        SingleTransactionQueue err = new SingleTransactionQueue(io);
        EchoProcessor         echo = new EchoProcessor();
        ResponseProcessor responses = new SimpleResponseProcessor();
        Context            context = new SimpleContext();
        Request            request = request();
        StreamTransactionProcessor system = new StreamTransactionProcessor(in.asInputStream(),out.asOutputStream(),err.asOutputStream(),io,context,echo,responses);
        
        in.put(request);
        system.call();

        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertEquals(request,response.request);
    }

    @Test
    public void Save_state_between_requests() throws Exception {
        SingleTransactionQueue in = new SingleTransactionQueue(io);
        SingleTransactionQueue out = new SingleTransactionQueue(io); 
        PrintStream            err = null;
        TimestampProcessor stamper = new TimestampProcessor();
        Context            context = new SimpleContext();
        SimpleTransactionProcessor processor = new SimpleTransactionProcessor(in,out,err,context,stamper,null);

        Timestamp t1 = new Timestamp(1);
        Request request1 = new Request("",t1);

        in.put(request1);
        processor.call();

        assertFalse(out.isEmpty());
        TimestampResponse response1 = (TimestampResponse) out.take();
        assertTimestampResponse(request1, t1, null, response1);

        processor = new SimpleTransactionProcessor(in,out,err,context,stamper,null);

        Timestamp t2 = new Timestamp(2);
        Request request2 = new Request("",t2);
        in.put(request2);
        processor.call();
        assertFalse(out.isEmpty());
        TimestampResponse response2 = (TimestampResponse) out.take();
        assertTimestampResponse(request2,t2,t1,response2);
    }

    private void assertTimestampResponse(Request request, Timestamp t, Timestamp previous, TimestampResponse response) {
        assertSame(request,response.request);
        assertSame(t,response.getTimestamp());
        assertSame(previous, response.previousRequest);
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
