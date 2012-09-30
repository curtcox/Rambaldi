package net.rambaldi.http;

import net.rambaldi.Log.Log;

import java.io.PrintStream;

/**
 * A wrapper for debugging HttpTransactionProcessors.
 */
public final class DebugHttpTransactionProcessor
    implements HttpTransactionProcessor
{
    private final HttpTransactionProcessor httpProcessor;
    private final Log out;

    public DebugHttpTransactionProcessor(HttpTransactionProcessor httpProcessor, Log out) {
        this.httpProcessor = httpProcessor;
        this.out = out;
    }

    @Override
    public HttpResponse process(HttpRequest request) throws Exception {
        out.info("HttpRequest: ");
        out.info(request);
        HttpResponse response = httpProcessor.process(request);
        out.info("HttpResponse: ");
        out.info(response);
        return response;
    }
}
