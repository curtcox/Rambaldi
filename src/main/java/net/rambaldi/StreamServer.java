package net.rambaldi;

import java.io.IOException;

/**
 * A server that reads commands and requests from an input stream and writes requests and responses to an
 * output stream.
 */
public interface StreamServer {

    boolean isUp();

    void start() throws ProcessCreationException;

    void stop();
}
