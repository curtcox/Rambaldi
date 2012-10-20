package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.time.Immutable;

import java.io.IOException;
import java.io.Serializable;

/**
 * For processing HttpRequestS.
 */
public interface HttpRequestProcessor
    extends Immutable, Serializable
{
    /**
     * Process the given request and produce a response.
     * Throw an exception if something goes wrong.
     */
    HttpResponse process(HttpRequest request, Context context) throws Exception;
}
