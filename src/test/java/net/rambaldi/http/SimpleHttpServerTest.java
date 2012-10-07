package net.rambaldi.http;

import net.rambaldi.process.FakeExecutorService;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    FakeExecutorService executor = new FakeExecutorService();

    class FakeHttpConnectionHandler implements HttpConnection.Handler {

        HttpConnection connection;
        Exception exception;
        @Override
        public void handle(HttpConnection connection) throws Exception {
            this.connection = connection;
            if (exception!=null) {
                throw exception;
            }
        }
    }

    class FakeHttpConnectionFactory
        implements HttpConnection.Factory
    {
        int acceptCalled;
        int connectionsToAccept;
        boolean acceptWasCalledFromExecutor;
        int port;
        public FakeHttpConnectionFactory(int port) throws IOException {
            super();
            this.port = port;
        }

        @Override
        public HttpConnection accept() throws IOException {
            acceptCalled++;
            acceptWasCalledFromExecutor = executor.isExecuting;
            if (acceptCalled>connectionsToAccept) {
                throw new IOException();
            }
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
        server = SimpleHttpServer.builder()
                .executor(executor).connections(connectionFactory).handler(handler).build();
        out = new ByteArrayOutputStream();
        connection = new SimpleHttpConnection(in,out);
    }

    @Test
    public void can_create() throws IOException {
        assertNotNull(SimpleHttpServer.builder().build());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_executor() throws IOException {
        SimpleHttpServer.builder().executor(null);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_connections() throws IOException {
        SimpleHttpServer.builder().connections(null);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_handler() throws IOException {
        SimpleHttpServer.builder().handler(null);
    }

    @Test
    public void start_starts_accepting_from_socket() throws IOException {
        server.start();
        assertEquals(1, connectionFactory.acceptCalled);
    }

    @Test
    public void start_accepts_two_connections_from_socket() throws IOException {
        connectionFactory.connectionsToAccept = 1;
        server.start();
        assertEquals(2,connectionFactory.acceptCalled);
    }

    @Test
    public void start_uses_executor_to_accept_from_socket() throws IOException {
        server.start();
        assertTrue(connectionFactory.acceptWasCalledFromExecutor);
    }

    @Test(expected = Exception.class)
    public void failed_process_causes_exception_to_be_thrown() throws Throwable {
        handler.exception = new Exception();
        server.start();
        if (executor.threw!=null) {
            throw executor.threw;
        }
    }

    @Test
    public void start_handler_to_be_invoked_with_connection() throws Throwable {
        connectionFactory.connectionsToAccept = 2;
        server.start();
        assertSame(connection,handler.connection);
    }

}
