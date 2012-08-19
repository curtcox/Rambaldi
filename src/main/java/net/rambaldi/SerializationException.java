package net.rambaldi;

import java.io.IOException;

/**
 * Something went wrong during object serialization.
 * @author Curt
 */
public final class SerializationException
    extends RuntimeException
{

    public SerializationException(IOException e) {
        super(e);
    }
    
}
