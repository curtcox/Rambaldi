package net.rambaldi;

import java.util.HashMap;
import java.util.Map;
import static junit.framework.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class SimpleTransactionProcessorTest {

    SingleTransactionQueue  in; 
    SingleTransactionQueue out; 
    SingleTransactionQueue err; 
    SimpleTransactionProcessor system;
    Context context;
    
    @Before
    public void before() {
             in = new SingleTransactionQueue(); 
            out = new SingleTransactionQueue(); 
            err = new SingleTransactionQueue(); 
        context = null;
    }

    SimpleTransactionProcessor system(RequestProcessor requestProcessor) {
        return new SimpleTransactionProcessor(in,out,err,context,requestProcessor,null);
    }
    
    SimpleTransactionProcessor system() {
        return new SimpleTransactionProcessor(in,out,err,context,new EchoProcessor(),null);
    }
    
    @Test
    public void process_takes_request_from_in() {
        Transaction transaction = new Request(null);
        in.put(transaction);
        system().process();
        assertEquals(transaction,in.take());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void process_throws_exception_for_unknown_transaction_type() {
        Transaction transaction = new Transaction(null){};
        in.put(transaction);
        system().process();
    }

    @Test
    public void process_echo_writes_requests_to_out() {
        Request request = new Request(null);
        in.put(request);
        system().process();
        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertEquals(request,response.request);
    }

    @Test
    public void process_uses_RequestProcessor_for_Requests() {
        final Request   request = new Request(null);
        final Map called = new HashMap();
        in.put(request);
        RequestProcessor processor = new RequestProcessor() {
            @Override
            public Response process(Request request, Context context) {
                called.put("request", request);
                return null;
            }
        };
        
        system(processor).process();
        
        assertEquals(request,called.get("request"));
    }

    @Test
    public void process_discards_RequestProcessor_null_responses() {
        final Request   request = new Request(null);
        in.put(request);
        RequestProcessor processor = new RequestProcessor() {
            @Override
            public Response process(Request request, Context context) {
                return null;
            }
        };
        
        assertTrue(out.isEmpty());
        system(processor).process();
        
        assertTrue(out.isEmpty());
    }

    @Test
    public void process_puts_RequestProcessor_non_null_responses_to_out() {
        final Request   request = new Request(null);
        final Response response = new Response(request);
        in.put(request);
        RequestProcessor processor = new RequestProcessor() {
            @Override
            public Response process(Request request, Context context) {
                return response;
            }
        };
        
        system(processor).process();
        
        assertFalse(out.isEmpty());
        assertEquals(response,out.take());
    }

}
