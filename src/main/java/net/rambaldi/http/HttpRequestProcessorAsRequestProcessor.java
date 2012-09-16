package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.process.Request;
import net.rambaldi.process.RequestProcessor;
import net.rambaldi.process.Response;

/**
 * Wraps a HttpRequestProcessor, to make it a RequestProcessor.
 */
public final class HttpRequestProcessorAsRequestProcessor
    implements RequestProcessor
{

    private final HttpRequestProcessor processor;

    public HttpRequestProcessorAsRequestProcessor(HttpRequestProcessor processor) {
        this.processor = processor;
    }

    @Override
    public Response process(Request request, Context context) {
        HttpRequest httpRequest = (HttpRequest) request;
        return processor.process(httpRequest,context);
    }
}
