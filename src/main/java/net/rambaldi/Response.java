package net.rambaldi;

/**
 * An immutable response to a request.
 * @author Curt
 */
public class Response
    extends Transaction
{
    public final Request request;
    
    public Response(String value,Request request) {
        super(value.getBytes(),request.timestamp);
        this.request = request;
    }

    public Response(byte[] bytes,Request request) {
        super(bytes,request.timestamp);
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Response that = (Response) o;
        return request.equals(that.request);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
