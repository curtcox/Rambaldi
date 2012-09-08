package net.rambaldi.process;

/**
 * An immutable response to a request.
 * @author Curt
 */
public class Response
    implements Transaction
{
    public final Request request;
    public final String value;
    private final Timestamp timestamp;
    
    public Response(String value,Request request) {
        this.request = request;
        this.value = value;
        this.timestamp = request.getTimestamp();
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        Response that = (Response) o;
        return request.equals(that.request);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
