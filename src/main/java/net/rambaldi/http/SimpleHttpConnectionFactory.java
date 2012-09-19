package net.rambaldi.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class SimpleHttpConnectionFactory
    implements HttpConnection.Factory, AutoCloseable
{
    private final ServerSocket serverSocket;

    public SimpleHttpConnectionFactory(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public HttpConnection accept() throws IOException {
        Socket socket = serverSocket.accept();

        return new SimpleHttpConnection(socket.getInputStream(), socket.getOutputStream());
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }
}
