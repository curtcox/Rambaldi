package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.process.Immutable;

import java.io.Serializable;

/**
 * For processing HttpRequestS.
 */
public interface HttpRequestProcessor
    extends Immutable, Serializable
{
    HttpResponse process(HttpRequest request, Context context);
}
