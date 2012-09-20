package net.rambaldi.http;

import net.rambaldi.process.Context;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class SimpleHttpTransactionProcessor
    implements HttpTransactionProcessor
{
    private final HttpRequestProcessor requestProcessor;
    private final Context context;

    public SimpleHttpTransactionProcessor(HttpRequestProcessor httpRequestProcessor, Context context) {
        this.requestProcessor = requireNonNull(httpRequestProcessor);
        this.context = requireNonNull(context);
    }

    @Override
    public HttpResponse process(HttpRequest request) throws Exception {
        return requestProcessor.process(request,context);
    }
}
