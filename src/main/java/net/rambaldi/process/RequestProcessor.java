package net.rambaldi.process;

import net.rambaldi.time.Immutable;

import java.io.Serializable;

/**
 * Something that processes a request.
 */
public interface RequestProcessor
    extends Immutable, Serializable
{

    /**
     * Process the request, using context and possibly producing a response.
     * @param request what to process
     * @param context state to use for processing
     * @return the Response, or null if there is none
     */
    Response process(Request request,Context context) throws Exception;
}
