package net.rambaldi.http;

import net.rambaldi.process.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

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

    public class FakeServerSocket
            extends ServerSocket
    {
        public boolean acceptWasCalled;
        public FakeSocket socket = new FakeSocket();
        public boolean acceptWasCalledFromExecutor;
        public int port;
        public FakeServerSocket(int port) throws IOException {
            super();
            this.port = port;
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

    @Test(expected = Exception.class)
    public void failed_process_causes_exception_to_be_thrown() throws Throwable {
        server.start();
        if (executor.threw!=null) {
            throw executor.threw;
        }
    }

    @Test
    public void start_causes_output_to_be_written_back_to_socket() throws Throwable {
        processor.response = HttpResponse.builder().request(request).build();
        server.start();
        assertTrue(out.toByteArray().length>0);
    }

    @Test
    public void start_causes_response_to_be_written_back_to_socket() throws Throwable {
        HttpResponse expected = HttpResponse.builder().request(request).build();
        processor.response = expected;
        server.start();
        String actual = new String(out.toByteArray());
        assertEquals(expected.toString(),actual);
    }

}
