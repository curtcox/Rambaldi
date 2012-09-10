package net.rambaldi.http;

import net.rambaldi.process.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import static java.util.Objects.requireNonNull;

public class SimpleHttpServer {

    private final TransactionProcessor processor;
    private final Executor executor;
    private final ServerSocket serverSocket;

    public SimpleHttpServer(Executor executor, TransactionProcessor processor, int port) throws IOException {
        this(executor,new ServerSocket(port),processor);
    }

    public SimpleHttpServer(Executor executor, ServerSocket serverSocket, TransactionProcessor processor) {
        this.executor = requireNonNull(executor);
        this.serverSocket = requireNonNull(serverSocket);
        this.processor = requireNonNull(processor);
    }

    public void start() {
        FutureTask command = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
//                Socket socket = serverSocket.accept();
//                InputStream in = socket.getInputStream();
//                TransactionSink toProcessor = new OutputStreamAsTransactionSink(processor.getInput(),io);
//                SingleTransactionQueue toProcessor = new SingleTransactionQueue(io);
//                toProcessor.put(request);
//
//                processor.getInput();
//
//                OutputStream out = socket.getOutputStream();
                return null;
            }
        });
        executor.execute(command);
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
