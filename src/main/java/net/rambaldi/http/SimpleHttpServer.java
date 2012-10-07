package net.rambaldi.http;

import net.rambaldi.process.SimpleContext;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static java.util.Objects.requireNonNull;

/**
 * A very simple HTTP server.
 */
public final class SimpleHttpServer
    implements AutoCloseable
{
    private final HttpConnection.Handler handler;
    private final ExecutorService executor;
    private final HttpConnection.Factory connectionFactory;
    private final Callable acceptCallable;
    public static final int DEFAULT_PORT = 80;

    private SimpleHttpServer(Builder builder) {
        this.handler = requireNonNull(builder.handler);
        this.executor = builder.executor;
        this.connectionFactory = builder.connections;
        acceptCallable = acceptNewSocket();
    }

    /**
     * For building SimpleHttpServerS.
     */
    public static final class Builder {
        private ExecutorService executor = Executors.newFixedThreadPool(2);
        private HttpConnection.Factory connections;
        private HttpConnection.Handler handler = new SimpleHttpConnectionHandler();

        /**
         * Note that the given ExecutorService is used both for accepting socket connections and servicing them.
         * Thus, a single-threaded ExecutorService will cause a single keep-alive connection to block accepting
         * and new connections.
         */
        public Builder executor(ExecutorService executor)              { this.executor    = requireNonNull(executor); return this;}
        public Builder connections(HttpConnection.Factory connections) { this.connections = requireNonNull(connections); return this;}
        public Builder handler(HttpConnection.Handler handler)         { this.handler     = requireNonNull(handler); return this;}

        public SimpleHttpServer build() {
            return new SimpleHttpServer(this);
        }
    }

    public static Builder builder() {
         return new Builder();
    }

    private Callable acceptNewSocket() {
        return new Callable() {
            @Override
            public Object call() throws Exception {
                while (!executor.isShutdown()) {
                    handle(connectionFactory.accept());
                }
                return null;
            }
        };
    }

    public void start() {
        executor.execute(new FutureTask(acceptCallable));
    }

    private void handle(final HttpConnection connection) throws Exception {
        executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                handler.handle(connection);
                return null;
            }
        });
    }

    @Override
    public void close() throws Exception {
        connectionFactory.close();
    }
}
