package net.rambaldi.http;

import net.rambaldi.process.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class SimpleHttpServer {
    private final HttpRequestProcessor processor;
    private final int port;

    public SimpleHttpServer(HttpRequestProcessor processor, int port) {
        this.processor = Objects.requireNonNull(processor);
        this.port = port;
    }

    public void start() {
    }
    //        Callable callable = new Callable() {
//            @Override
//            public Object call() throws Exception {
//                try {
//                    startAnEchoServer(port);
//                return null;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw e;
//                }
//            }
//        };
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.submit(callable);

//    private void startAnEchoServer(int port) throws IOException {
//        ServerSocket serverSocket = new ServerSocket(port);
//        Socket socket = serverSocket.accept();
//        HttpEchoProcessor echo = new HttpEchoProcessor();
//        ResponseProcessor responses = new SimpleResponseProcessor();
//        Context context = new SimpleContext();
//        HttpRequestReader reader = new HttpRequestReader(socket.getInputStream());
//        HttpResponseWriter writer = new HttpResponseWriter(socket.getOutputStream());
//        PrintStream err = System.err;
//        TransactionProcessor system = new SimpleTransactionProcessor(reader,writer,err,context,echo,responses);
//        system.process();
//    }
}
