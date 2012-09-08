package net.rambaldi.process;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import static junit.framework.Assert.*;

import net.rambaldi.process.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class SimpleTransactionProcessorTest {

    final IO io = new SimpleIO();

    SingleTransactionQueue in;
    SingleTransactionQueue out; 
    PrintStream err;
    Context context;
    
    @Before
    public void before() {
             in = new SingleTransactionQueue(io); 
            out = new SingleTransactionQueue(io); 
            err = System.err;
        context = null;
    }

    SimpleTransactionProcessor processor(RequestProcessor requestProcessor) {
        return new SimpleTransactionProcessor(in,out,err,context,requestProcessor,null);
    }
    
    SimpleTransactionProcessor processor() {
        return new SimpleTransactionProcessor(in,out,err,context,new EchoProcessor(),null);
    }
    
    @Test
    public void process_takes_request_from_in() {
        Transaction transaction = request();
        in.put(transaction);
        assertFalse(in.isEmpty());
        processor().process();
        assertTrue(in.isEmpty());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void process_throws_exception_for_unknown_transaction_type() {
        Transaction transaction = new Transaction(){

            @Override
            public Timestamp getTimestamp() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        in.put(transaction);
        processor().process();
    }

    @Test
    public void process_echo_writes_requests_to_out() {
        Request request = request();
        in.put(request);
        processor().process();
        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertEquals(request,response.request);
    }

    @Test
    public void process_uses_RequestProcessor_for_Requests() {
        final Request   request = request();
        final Map called = new HashMap();
        in.put(request);
        RequestProcessor processor = new RequestProcessor() {
            @Override
            public Response process(Request request, Context context) {
                called.put("request", request);
                return null;
            }
        };
        
        processor(processor).process();
        
        assertEquals(request,called.get("request"));
    }

    @Test
    public void process_discards_RequestProcessor_null_responses() {
        final Request   request = request();
        in.put(request);
        RequestProcessor processor = new RequestProcessor() {
            @Override
            public Response process(Request request, Context context) {
                return null;
            }
        };
        
        assertTrue(out.isEmpty());
        processor(processor).process();
        
        assertTrue(out.isEmpty());
    }

    @Test
    public void process_puts_RequestProcessor_non_null_responses_to_out() {
        final Request   request = request();
        final Response response = new Response("",request);
        in.put(request);
        RequestProcessor processor = new RequestProcessor() {
            @Override
            public Response process(Request request, Context context) {
                return response;
            }
        };
        
        processor(processor).process();
        
        assertFalse(out.isEmpty());
        assertEquals(response,out.take());
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }
}
