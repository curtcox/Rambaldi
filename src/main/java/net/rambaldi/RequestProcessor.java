package net.rambaldi;

/**
 * Something that processes a request.
 */
public interface RequestProcessor
    extends Immutable, java.io.Serializable
{

    /**
     * Process the request, using context and possibly producing a response.
     * @param request what to process
     * @param context state to use for processing
     * @return the Response, or null if there is none
     */
    Response process(Request request,Context context);
}
