package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.time.Immutable;

import java.io.Serializable;

/**
 * For processing HttpRequestS.
 */
public interface HttpRequestProcessor
    extends Immutable, Serializable
{
    /**
     * Process the given request and produce a response.
     */
    HttpResponse process(HttpRequest request, Context context);
}
