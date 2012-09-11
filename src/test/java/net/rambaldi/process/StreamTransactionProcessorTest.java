package net.rambaldi.process;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
public class StreamTransactionProcessorTest {

    final IO io = new SimpleIO();

    InputStream in = new ByteArrayInputStream(new byte[0]);
    OutputStream out = new ByteArrayOutputStream();
    OutputStream err = new ByteArrayOutputStream();

    SingleTransactionQueue  inQueue;
    SingleTransactionQueue outQueue;
    Context context;
    RequestProcessor requests = new EchoProcessor();
    ResponseProcessor responses = new SimpleResponseProcessor();

    @Before
    public void before() {
             inQueue = new SingleTransactionQueue(io);
            outQueue = new SingleTransactionQueue(io);
             context = new SimpleContext();
    }

    StreamTransactionProcessor processor(RequestProcessor requestProcessor) {
        return new StreamTransactionProcessor(
                inQueue.asInputStream(),outQueue.asOutputStream(),err,io,
                context,requestProcessor,responses);
    }
    
    StreamTransactionProcessor processor() {
        return new StreamTransactionProcessor(
                inQueue.asInputStream(),outQueue.asOutputStream(),err,io,
                context,new EchoProcessor(),responses);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_in() {
        new StreamTransactionProcessor(null,out,err,io,context,requests,responses);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_out() {
        new StreamTransactionProcessor(in,null,err,io,context,requests,responses);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_err() {
        new StreamTransactionProcessor(in,out,null,io,context,requests,responses);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_io() {
        new StreamTransactionProcessor(in,out,err,null,context,requests,responses);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_context() {
        new StreamTransactionProcessor(in,out,err,io,null,requests,responses);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_requests() {
        new StreamTransactionProcessor(in,out,err,io,context,null,responses);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_responses() {
        new StreamTransactionProcessor(in,out,err,io,context,requests,null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void call_throws_exception_for_unknown_transaction_type() {
        Transaction transaction = new UnsupportedTransaction();
        inQueue.put(transaction);
        processor().call();
    }

    @Test
    public void call_takes_request_from_in() {
        Transaction transaction = new Request("",new Timestamp(0));
        inQueue.put(transaction);

        processor().call();

        assertTrue(inQueue.isEmpty());
    }
    

    @Test
    public void call_echo_writes_requests_to_out() {
        Request request = new Request("",new Timestamp(0));
        inQueue.put(request);

        processor().call();

        assertFalse(outQueue.isEmpty());
        Response response = (Response) outQueue.take();
        assertEquals(request,response.request);
    }

    @Test
    public void call_uses_RequestProcessor_for_Requests() {
        final Request   request = new Request("",new Timestamp(0));
        final Map called = new HashMap();
        inQueue.put(request);
        RequestProcessor processor = new RequestProcessor() {
            @Override
            public Response process(Request request, Context context) {
                called.put("request", request);
                return null;
            }
        };
        
        processor(processor).call();
        
        assertEquals(request,called.get("request"));
    }

    @Test
    public void call_discards_RequestProcessor_null_responses() {
        final Request   request = new Request("",new Timestamp(0));
        inQueue.put(request);
        RequestProcessor processor = new RequestProcessor() {
            @Override
            public Response process(Request request, Context context) {
                return null;
            }
        };
        
        assertTrue(outQueue.isEmpty());
        processor(processor).call();
        
        assertTrue(outQueue.isEmpty());
    }

    @Test
    public void call_puts_RequestProcessor_non_null_responses_to_out() {
        final Request   request = new Request("",new Timestamp(0));
        final Response response = new Response("",request);
        inQueue.put(request);
        RequestProcessor processor = new RequestProcessor() {
            @Override
            public Response process(Request request, Context context) {
                return response;
            }
        };
        
        processor(processor).call();
        
        assertFalse(outQueue.isEmpty());
        assertEquals(response,outQueue.take());
    }

    static class UnsupportedTransaction implements Transaction {
        @Override
        public Timestamp getTimestamp() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
