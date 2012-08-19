package net.rambaldi;

/**
 * An immutable Request.
 * @author Curt
 */
public final class Request
    extends Transaction
{
    public Request(Timestamp timestamp) {
        super(timestamp);
    }
}
