package net.rambaldi.process;

/**
 * Something went wrong during object deserialization.
 * @author Curt
 */
public final class DeserializationException
    extends RuntimeException
{

    public DeserializationException(Exception e) {
        super(e);
    }
    
}
