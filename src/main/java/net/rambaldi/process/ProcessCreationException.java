package net.rambaldi.process;

import java.io.IOException;

/**
 * Something went wrong while trying to create a process.
 */
public class ProcessCreationException
    extends Exception
{
    public ProcessCreationException(Throwable t) {
        super(t);
    }
}
