package net.rambaldi.http;

import net.rambaldi.http.HttpRequest;
import net.rambaldi.http.HttpResponse;
import net.rambaldi.http.HttpTransactionProcessor;
import net.rambaldi.process.TransactionProcessor;

public class TransactionProcessorAsHttpTransactionProcessor implements HttpTransactionProcessor {

    private final TransactionProcessor processor;

    public TransactionProcessorAsHttpTransactionProcessor(TransactionProcessor processor) {
        this.processor = processor;
    }

    @Override
    public HttpResponse process(HttpRequest request) throws Exception {
        return (HttpResponse) processor.process(request);
    }
}
