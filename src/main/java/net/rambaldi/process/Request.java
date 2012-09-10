package net.rambaldi.process;

import java.util.Objects;

/**
 * An immutable Request.
 * @author Curt
 */
public class Request
    implements Transaction
{
    public final String value;
    private final Timestamp timestamp;
    
    public Request(String value, Timestamp timestamp) {
        this.value = Objects.requireNonNull(value);
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    @Override
    final public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        Request that = (Request) o;
        return timestamp.equals(that.timestamp) &&
               value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode() ^ timestamp.hashCode();
    }

    @Override
    public String toString() {
        return "request(" + timestamp + "," + value + ")";
    }
}
