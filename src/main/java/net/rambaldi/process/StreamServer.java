package net.rambaldi.process;

import java.io.*;

/**
 * A server that reads commands and requests from an input stream and writes requests and responses to an
 * output stream.
 */
public interface StreamServer {

    /**
     * Return true if this server is up.
     */
    boolean isUp();

    /**
     * Start processing transactions.
     * @throws ProcessCreationException
     */
    void start() throws ProcessCreationException;

    /*
     * Stop processing transactions.
     */
    void stop();

    /**
     * Return the stream where transactions that need to be processed are written to.
     */
    OutputStream getInput();

    /**
     * Return the stream where processed transactions are written to.
     */
    InputStream getOutput();
}
