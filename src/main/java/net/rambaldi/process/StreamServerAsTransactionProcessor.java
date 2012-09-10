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

}
