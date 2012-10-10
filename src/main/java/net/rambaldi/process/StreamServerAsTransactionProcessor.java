package net.rambaldi.process;

import net.rambaldi.Log.Log;

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
    private final Log log;

    public StreamServerAsTransactionProcessor(StreamServer streamServer, IO io, Log log) {
        this.log = log;
        requireNonNull(streamServer);
        out = streamServer.getInput();
        sink = new OutputStreamAsTransactionSink(out,io);
        source = new InputStreamAsTransactionSource(streamServer.getOutput(),io);
    }

    @Override
    public Response process(Request request) throws Exception {
        try {
            sink.put(request);  //  <<<<<<< serialization error here causes mysterious failure -- better handling is needed
            out.flush();
            return (Response) source.take();
        } catch (Exception e) {
            log.throwable(e);
            throw e;
        }
    }

}
