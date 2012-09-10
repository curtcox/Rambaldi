package net.rambaldi.http;

import net.rambaldi.process.FakeTransactionProcessor;
import net.rambaldi.process.Timestamp;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

import static org.junit.Assert.*;

public class SimpleHttpServerTest {

    int port = 42;
    ByteArrayInputStream in;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    FakeServerSocket serverSocket;
    FakeTransactionProcessor processor = new FakeTransactionProcessor();
    HttpRequest request;
    SimpleHttpServer server;
    FakeExecutor executor = new FakeExecutor();
    class FakeExecutor implements Executor {
        boolean isExecuting;
        @Override
        public void execute(Runnable command) {
            isExecuting = true;
            try {
                command.run();
            } finally {
                isExecuting = false;
            }
        }
    };

    public class FakeServerSocket
            extends ServerSocket
    {
        public boolean acceptWasCalled;
        public FakeSocket socket = new FakeSocket();
        public boolean acceptWasCalledFromExecutor;

        public FakeServerSocket(int port) throws IOException {
            super();
        }

        @Override
        public Socket accept() throws IOException {
            acceptWasCalled = true;
            acceptWasCalledFromExecutor = executor.isExecuting;
            return socket;
        }
    }

    @Before
    public void before() throws IOException {
        String requestString = "GET / HTTP/1.1\r\n\r\n";
        in = new ByteArrayInputStream(requestString.getBytes());
        request = new HttpRequest(requestString,new Timestamp(7), HttpRequest.Method.GET);
        serverSocket = new FakeServerSocket(port);
        serverSocket.socket.input = in;
        serverSocket.socket.output = out;
        server = new SimpleHttpServer(executor,serverSocket, processor);
    }

    @Test
    public void can_create() throws IOException {
        new SimpleHttpServer(executor,processor,0);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_executor() throws IOException {
        new SimpleHttpServer(null,processor,0);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_processor() throws IOException {
        new SimpleHttpServer(executor,null,0);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_server_socket() throws IOException {
        new SimpleHttpServer(executor,null,processor);
    }

    @Test
    public void start_starts_accepting_from_socket() throws IOException {
        server.start();
        assertTrue(serverSocket.acceptWasCalled);
    }

    @Test
    public void start_uses_executor_to_accept_from_socket() throws IOException {
        server.start();
        assertTrue(serverSocket.acceptWasCalledFromExecutor);
    }

    @Test
    public void start_causes_output_to_be_written_back_to_socket() throws IOException {
        server.start();
        assertTrue(out.toByteArray().length>0);
    }

//    @Test
//    public void start_causes_request_to_be_written_to_server() throws IOException {
//        server.start();
//        assertTrue(processor.input.toByteArray().length > 0);
//    }
}
