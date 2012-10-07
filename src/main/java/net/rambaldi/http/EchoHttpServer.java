package net.rambaldi.http;

import net.rambaldi.Log.SimpleLog;
import net.rambaldi.process.Context;
import net.rambaldi.process.SimpleContext;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A working HTTP echo server.
 * This is for debugging and seeing how the pieces of this package work with each other.
 */
public final class EchoHttpServer {

    public static SimpleHttpServer newDebugServer(int port) throws IOException {
        HttpTransactionProcessor httpProcessor =
                new SimpleHttpTransactionProcessor(new HttpRequestEchoProcessor(), new SimpleContext());
        httpProcessor = new DebugHttpTransactionProcessor(httpProcessor,new SimpleLog("HttpTransactionProcessor",System.err));
        HttpConnection.Factory connectionFactory = new SimpleHttpConnectionFactory(port);
        connectionFactory = new DebugHttpConnectionFactory(connectionFactory,new SimpleLog("Connection Factory",System.err));
        SimpleHttpServer server = createNewServer(httpProcessor, connectionFactory);
        server.start();
        return server;
    }

    private static SimpleHttpServer createNewServer(HttpTransactionProcessor httpProcessor, HttpConnection.Factory connectionFactory) {
        return SimpleHttpServer.builder()
                .connections(connectionFactory)
                .handler(new SimpleHttpConnectionHandler(httpProcessor))
                .build();
    }

    public static SimpleHttpServer newServer(int port) throws IOException {
        SimpleHttpServer server = createNewServer(
            new SimpleHttpTransactionProcessor(new HttpRequestEchoProcessor(), new SimpleContext()),
            new SimpleHttpConnectionFactory(port));
        server.start();
        return server;
    }

    public static void main(String[] args) throws IOException {
        int port = (args.length==0)
            ? 80
            : Integer.parseInt(args[0]);
        newDebugServer(port);
    }

}
