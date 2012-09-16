package net.rambaldi.http;

import net.rambaldi.process.*;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class HttpRequestProcessorAsRequestProcessorTest {

    Context context = new SimpleContext();
    HttpRequest request = HttpRequest.builder().build();
    HttpResponse response = HttpResponse.builder().request(request).build();
    FakeHttpRequestProcessor wrapped = new FakeHttpRequestProcessor();

    class FakeHttpRequestProcessor implements HttpRequestProcessor {
        HttpRequest request;
        Context context;
        @Override
        public HttpResponse process(HttpRequest request, Context context) {
            this.request = request;
            this.context = context;
            return response;
        }
    };

    @Test
    public void process_returns_response_from_wrapped_processor() throws Exception {
        RequestProcessor wrapper = new HttpRequestProcessorAsRequestProcessor(wrapped);
        Response response = wrapper.process(request,context);
        assertSame(this.response,response);
    }

    @Test
    public void process_supplies_wrapped_processor_with_context() throws Exception {
        RequestProcessor wrapper = new HttpRequestProcessorAsRequestProcessor(wrapped);
        wrapper.process(request,context);
        assertSame(context,wrapped.context);
    }

    @Test
    public void process_supplies_wrapped_processor_with_request() throws Exception {
        RequestProcessor wrapper = new HttpRequestProcessorAsRequestProcessor(wrapped);
        wrapper.process(request,context);
        assertSame(request,wrapped.request);
    }
}
