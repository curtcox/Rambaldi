package net.rambaldi;

/**
 * A server that reads commands and requests from an input stream and writes requests and responses to an
 * output stream.
 */
public interface StreamServer {

    boolean isUp();

    void start();

    void stop();
}
