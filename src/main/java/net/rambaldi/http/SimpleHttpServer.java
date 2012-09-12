package net.rambaldi.http;

import net.rambaldi.process.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import static java.util.Objects.requireNonNull;

/**
 * A very simple HTTP server.
 */
public final class SimpleHttpServer {

    private final TransactionProcessor processor;
    private final Executor executor;
    private final ServerSocket serverSocket;
    private final Callable acceptCallable;

    public SimpleHttpServer(Executor executor, TransactionProcessor processor, int port) throws IOException {
        this(executor,new ServerSocket(port),processor);
    }

    public SimpleHttpServer(Executor executor, ServerSocket serverSocket, TransactionProcessor processor) {
        this.executor = requireNonNull(executor);
        this.serverSocket = requireNonNull(serverSocket);
        this.processor = requireNonNull(processor);
        acceptCallable = acceptNewSocket();
    }

    private Callable acceptNewSocket() {
        return new Callable() {
            @Override
            public Object call() throws Exception {
                acceptSocket();
                return null;
            }
        };
    }

    public void start() {
        executor.execute(new FutureTask(acceptCallable));
    }

    private void acceptSocket() throws Exception {
        Socket             socket = serverSocket.accept();
        HttpRequestReader  reader = new HttpRequestReader(socket.getInputStream());
        Response         response = processor.process(reader.take());
        HttpResponseWriter writer = new HttpResponseWriter(socket.getOutputStream());
        writer.put(response);
    }
}
