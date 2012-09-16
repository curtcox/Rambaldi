package net.rambaldi.http;

import net.rambaldi.process.Request;
import net.rambaldi.process.Response;
import net.rambaldi.process.Timestamp;
import net.rambaldi.process.TransactionProcessor;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class TransactionProcessorAsHttpTransactionProcessorTest {

    HttpRequest request = HttpRequest.builder().build();
    HttpResponse response = HttpResponse.builder().request(request).build();
    TransactionProcessor processor = new TransactionProcessor() {
        @Override
        public Response process(Request request) throws Exception {
            return response;
        }
    };

    TransactionProcessorAsHttpTransactionProcessor httpProcessor =
            new TransactionProcessorAsHttpTransactionProcessor(processor);

    @Test
    public void process_request() throws Exception {

        HttpResponse actual = httpProcessor.process(request);

        assertSame(response,actual);
    }
}
