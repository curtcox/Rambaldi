package net.rambaldi;

/**
 * An immutable response to a request.
 * @author Curt
 */
public class Response
    extends Transaction
{
    public final Request request;
    
    public Response(Request request) {
        this.request = request;
    }
}
