package net.rambaldi.http;

import net.rambaldi.Log.SimpleLog;
import net.rambaldi.process.Context;
import net.rambaldi.process.SimpleContext;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An HTTP echo server.
 * This is for debugging and seeing how the pieces work with each other.
 */
public final class EchoHttpServer {

    public static SimpleHttpServer newDebugServer(int port) throws IOException {
        HttpRequestProcessor httpRequestProcessor = new HttpRequestEchoProcessor();
        Context context = new SimpleContext();
        HttpTransactionProcessor httpProcessor = new SimpleHttpTransactionProcessor(httpRequestProcessor,context);
        httpProcessor = new DebugHttpTransactionProcessor(httpProcessor,new SimpleLog("HttpTransactionProcessor",System.err));
        HttpConnection.Factory connectionFactory = new SimpleHttpConnectionFactory(port);
        connectionFactory = new DebugHttpConnectionFactory(connectionFactory,new SimpleLog("Connection Factory",System.err));
        ExecutorService executor = Executors.newFixedThreadPool(2);
        HttpConnection.Handler handler = new SimpleHttpConnectionHandler(httpProcessor);
        SimpleHttpServer server = new SimpleHttpServer(executor,connectionFactory,handler);
        server.start();
        return server;
    }

    public static SimpleHttpServer newServer(int port) throws IOException {
        HttpTransactionProcessor httpProcessor =
                new SimpleHttpTransactionProcessor(new HttpRequestEchoProcessor(), new SimpleContext());
        SimpleHttpServer server = new SimpleHttpServer(
                Executors.newFixedThreadPool(2),
                new SimpleHttpConnectionFactory(port),
                new SimpleHttpConnectionHandler(httpProcessor));
        server.start();
        return server;
    }

}
