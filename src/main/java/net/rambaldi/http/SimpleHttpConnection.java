package net.rambaldi.http;

import java.io.InputStream;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

public final class SimpleHttpConnection
    implements HttpConnection
{
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public SimpleHttpConnection(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = requireNonNull(inputStream);
        this.outputStream = requireNonNull(outputStream);
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
