package net.rambaldi;

import java.util.HashMap;
import java.util.Map;
import static junit.framework.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tests.acceptance.Copier;

/**
 *
 * @author Curt
 */
public class StreamTransactionProcessorTest {

    SingleTransactionQueue  in; 
    SingleTransactionQueue out; 
    SingleTransactionQueue err; 
    Context context;
    
    @Before
    public void before() {
             in = new SingleTransactionQueue(); 
            out = new SingleTransactionQueue(); 
            err = new SingleTransactionQueue(); 
        context = null;
    }

    StreamTransactionProcessor processor(RequestProcessor requestProcessor) {
        return new StreamTransactionProcessor(
                in.asInputStream(),out.asOutputStream(),err.asOutputStream(),
                context,requestProcessor,null);
    }
    
    StreamTransactionProcessor processor() {
        return new StreamTransactionProcessor(
                in.asInputStream(),out.asOutputStream(),err.asOutputStream(),
                context,new EchoProcessor(),null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void process_throws_exception_when_transaction_is_not_supported() {
        Transaction transaction = transaction();
        in.put(transaction);
        processor().process();
        assertEquals(transaction,in.take());
    }

    @Test(expected=IllegalArgumentException.class)
    public void process_throws_exception_for_unknown_transaction_type() {
        Transaction transaction = transaction();
        in.put(transaction);
        processor().process();
    }

    @Test
    public void process_takes_request_from_in() {
        Transaction transaction = new Request("",new Timestamp(0));
        in.put(transaction);
        processor().process();
        assertEquals(transaction,in.take());
    }
    

    @Test
    public void process_echo_writes_requests_to_out() {
        Request request = new Request("",new Timestamp(0));
        in.put(request);
        processor().process();
        assertFalse(out.isEmpty());
        Response response = (Response) out.take();
        assertEquals(request,response.request);
    }

    @Test
    public void process_uses_RequestProcessor_for_Requests() {
        final Request   request = new Request("",new Timestamp(0));
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
        final Request   request = new Request("",new Timestamp(0));
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
        final Request   request = new Request("",new Timestamp(0));
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

    @Test
    public void is_serializable() throws Exception {
        StreamTransactionProcessor processor = processor();
        StreamTransactionProcessor copy = Copier.copy(processor);
        assertTrue(copy instanceof StreamTransactionProcessor);
    }

    private Transaction transaction() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
