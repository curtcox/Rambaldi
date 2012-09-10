package net.rambaldi.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FakeSocket
    extends Socket
{
    public InputStream input;
    public OutputStream output;

    @Override
    public InputStream getInputStream() throws IOException {
        return input;
    }

    public OutputStream getOutputStream() throws IOException {
        return output;
    }
}