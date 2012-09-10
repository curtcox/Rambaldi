package net.rambaldi.process;

import java.io.IOException;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

/**
 * Wraps a StreamServer, so it can be used as a TransactionProcessor.
 */
public final class StreamServerAsTransactionProcessor
    implements TransactionProcessor
{
    private final TransactionSink sink;
    private final OutputStream out;
    private final TransactionSource source;

    public StreamServerAsTransactionProcessor(StreamServer streamServer, IO io) {
        requireNonNull(streamServer);
        out = streamServer.getInput();
        sink = new OutputStreamAsTransactionSink(out,io);
        source = new InputStreamAsTransactionSource(streamServer.getOutput(),io);
    }

    @Override
    public void process() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response process(Request request) throws Exception {
        sink.put(request);
        out.flush();
        return (Response) source.take();
    }

//    @Test
//    public void Read_from_standard_in_and_write_to_standard_out() throws Exception {
//        Request request = request();
//
//        StreamServer server = TransactionProcessors.newExternal(state);
//        assertFalse(server.isUp());
//
//        server.start();
//        assertTrue(server.isUp());
//
//        TransactionSink sink = new OutputStreamAsTransactionSink(server.getInput(),io);
//        TransactionSource source = new InputStreamAsTransactionSource(server.getOutput(),io);
//
//        sink.put(request);
//        server.getInput().flush();
//        Thread.sleep(1000);
//
//        Response response = (Response) source.take();
//        assertEquals(request,response.request);
//
//        server.stop();
//        assertFalse(server.isUp());
//    }

}
