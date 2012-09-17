package net.rambaldi.http;

import net.rambaldi.process.Context;

public final class SimpleHttpTransactionProcessor
    implements HttpTransactionProcessor
{
    private final HttpRequestProcessor requestProcessor;
    private final Context context;

    public SimpleHttpTransactionProcessor(HttpRequestProcessor httpRequestProcessor, Context context) {
        this.requestProcessor = httpRequestProcessor;
        this.context = context;
    }

    @Override
    public HttpResponse process(HttpRequest request) throws Exception {
        return requestProcessor.process(request,context);
    }
}
