package net.rambaldi.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FakeHttpConnection
    implements HttpConnection
{
    public InputStream input;
    public OutputStream output;

    @Override
    public InputStream getInputStream() {
        return input;
    }

    public OutputStream getOutputStream() {
        return output;
    }
}