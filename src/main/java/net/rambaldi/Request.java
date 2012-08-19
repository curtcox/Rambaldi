package net.rambaldi;

/**
 * An immutable Request.
 * @author Curt
 */
public class Request
    extends Transaction
{
    public Request(String value, Timestamp timestamp) {
        super(value.getBytes(),timestamp);
    }
}
