package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.process.SimpleContext;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.*;

public class SimpleHttpServerTest {

    int port = 42;
    ByteArrayInputStream in;
    ByteArrayOutputStream out;
    FakeHttpConnectionFactory connectionFactory;
    SimpleHttpConnection connection;
    FakeHttpConnectionHandler handler = new FakeHttpConnectionHandler();
    HttpRequest request;
    SimpleHttpServer server;
    FakeExecutor executor = new FakeExecutor();

    class FakeHttpConnectionHandler implements HttpConnection.Handler {

        HttpConnection connection;
        @Override
        public void handle(HttpConnection connection) throws Exception {
            this.connection = connection;
        }
    }

    class FakeExecutor implements Executor {
        boolean isExecuting;
        ExecutionException threw;
        boolean interrupted;
        @Override
        public void execute(Runnable command) {
            isExecuting = true;
            FutureTask task = (FutureTask) command;
            try {
                task.run();
                task.get();
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (ExecutionException e) {
                threw = e;
            } finally {
                isExecuting = false;
            }
        }
    };

    class FakeHttpConnectionFactory
        implements HttpConnection.Factory
    {
        boolean acceptWasCalled;
        boolean acceptWasCalledFromExecutor;
        int port;
        public FakeHttpConnectionFactory(int port) throws IOException {
            super();
            this.port = port;
        }

        @Override
        public HttpConnection accept() throws IOException {
            acceptWasCalled = true;
            acceptWasCalledFromExecutor = executor.isExecuting;
            return connection;
        }

        @Override
        public void close() throws Exception {
        }
    }

    @Before
    public void before() throws IOException {
        String requestString = "GET / HTTP/1.1\r\n\r\n";
        in = new ByteArrayInputStream(requestString.getBytes());
        request = HttpRequest.builder().build();
        connectionFactory = new FakeHttpConnectionFactory(port);
        server = new SimpleHttpServer(executor, connectionFactory, handler);
        out = new ByteArrayOutputStream();
        connection = new SimpleHttpConnection(in,out);
    }

    @Test
    public void can_create() throws IOException {
        new SimpleHttpServer(executor, connectionFactory,handler);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_executor() throws IOException {
        new SimpleHttpServer(null, connectionFactory,handler);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_processor() throws IOException {
        new SimpleHttpServer(executor, connectionFactory,null);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_server_socket() throws IOException {
        new SimpleHttpServer(executor,null,handler);
    }

    @Test
    public void start_starts_accepting_from_socket() throws IOException {
        server.start();
        assertTrue(connectionFactory.acceptWasCalled);
    }

    @Test
    public void start_uses_executor_to_accept_from_socket() throws IOException {
        server.start();
        assertTrue(connectionFactory.acceptWasCalledFromExecutor);
    }

    @Test(expected = Exception.class)
    public void failed_process_causes_exception_to_be_thrown() throws Throwable {
        server.start();
        if (executor.threw!=null) {
            throw executor.threw;
        }
    }

    @Test
    public void start_handler_to_be_invoked_with_context() throws Throwable {
        server.start();
        assertSame(connection,handler.connection);
    }

}
