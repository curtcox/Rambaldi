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

    private final HttpTransactionProcessor processor;
    private final Executor executor;
    private final ServerSocket serverSocket;
    private final Callable acceptCallable;

    public SimpleHttpServer(Executor executor, HttpTransactionProcessor processor, int port) throws IOException {
        this(executor,new ServerSocket(port),processor);
    }

    public SimpleHttpServer(Executor executor, ServerSocket serverSocket, HttpTransactionProcessor processor) {
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
        System.out.println("starting");
        executor.execute(new FutureTask(acceptCallable));
        System.out.println("executing");
    }

    private void acceptSocket() throws Exception {
        System.out.println("accepting");
        Socket             socket = serverSocket.accept();
        System.out.println("accepted");
        HttpRequestReader  reader = new HttpRequestReader(socket.getInputStream());
        System.out.println("read");
        HttpResponse     response = processor.process(reader.take());
        System.out.println("response");
        HttpResponseWriter writer = new HttpResponseWriter(socket.getOutputStream());
        writer.put(response);
        System.out.println("response written");

    }
}
