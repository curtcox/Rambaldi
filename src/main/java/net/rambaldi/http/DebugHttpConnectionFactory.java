package net.rambaldi.http;

import net.rambaldi.Log.Log;

import java.io.IOException;

public final class DebugHttpConnectionFactory
    implements HttpConnection.Factory
{
    private final HttpConnection.Factory factory;
    private final Log out;

    public DebugHttpConnectionFactory(HttpConnection.Factory factory, Log out) {
        this.factory = factory;
        this.out = out;
    }

    @Override
    public HttpConnection accept() throws IOException {
        out.info("Accepting connection");
        HttpConnection connection = factory.accept();
        out.info("Accepted connection");
        return connection;
    }

    @Override
    public void close() throws Exception {
        factory.close();
    }
}
