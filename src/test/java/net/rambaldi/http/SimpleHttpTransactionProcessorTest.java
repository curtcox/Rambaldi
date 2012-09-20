package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.process.SimpleContext;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class SimpleHttpTransactionProcessorTest {

    HttpRequestProcessor httpRequestProcessor;
    Context context;

    @Test
    public void can_create() {
        httpRequestProcessor = new HttpRequestEchoProcessor();
        context = new SimpleContext();
        new SimpleHttpTransactionProcessor(httpRequestProcessor,context);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_request_processor() {
        context = new SimpleContext();
        new SimpleHttpTransactionProcessor(httpRequestProcessor,context);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_context() {
        httpRequestProcessor = new HttpRequestEchoProcessor();
        new SimpleHttpTransactionProcessor(httpRequestProcessor,context);
    }

    @Test
    public void process_uses_processor_from_constructor() throws Exception {
        httpRequestProcessor = new HttpRequestEchoProcessor();
        context = new SimpleContext();
        HttpTransactionProcessor processor = new SimpleHttpTransactionProcessor(httpRequestProcessor,context);
        HttpRequest request = HttpRequest.builder().build();

        HttpResponse response = processor.process(request);

        assertSame(request,response.request);
    }

}
