package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.process.Immutable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * A connection between a Http client and server.
 * Perhaps a web browser and a web server.  Perhaps not.
 */
public interface HttpConnection
    extends AutoCloseable
{
    /**
     * Something that accepts HttpConnections.
     */
    interface Factory extends AutoCloseable {
        HttpConnection accept() throws IOException;
    }

    /*
     * Something that handles or services HttpConnections.
     */
    interface Handler extends Immutable, Serializable
    {
        /**
         * Calling this method should read a HttpRequest from the input and write the HttpResponse to the output.
         * Depending on the request, everything might also be closed.
         */
        void handle(HttpConnection connection, Context context) throws Exception;
    }

    boolean isClosed();

    /**
     * Where HttpRequestS are read from.
     */
    InputStream getInputStream();

    /**
     * Where HttpResponsesS are written to.
     */
    OutputStream getOutputStream();
}
