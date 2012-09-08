package net.rambaldi.process;

import java.io.InputStream;
import java.io.OutputStream;
import static java.util.Objects.*;
/**
 * A TransactionProcessor that uses streams for IO.
 * @author Curt
 */
public final class StreamTransactionProcessor
    implements TransactionProcessor
{
    public final TransactionSource in;
    public final TransactionSink out;
    public final TransactionSink err;
    public final Context context;
    public final RequestProcessor requests;
    public final ResponseProcessor responses;

    public StreamTransactionProcessor(
            InputStream in, OutputStream out, OutputStream err, IO io,
            Context context, RequestProcessor requests, ResponseProcessor responses)
    {
        requireNonNull(io);
        this.in = new InputStreamAsTransactionSource(requireNonNull(in),io);
        this.out = new OutputStreamAsTransactionSink(requireNonNull(out),io);
        this.err = new OutputStreamAsTransactionSink(requireNonNull(err),io);
        this.context = requireNonNull(context);
        this.requests = requireNonNull(requests);
        this.responses = requireNonNull(responses);
    }

    @Override
    public void process() {
        Transaction transaction = in.take();
        if (transaction instanceof Request) {
            Request request = (Request) transaction;
            Response response = requests.process(request, context);
            if (response!=null) {
                out.put(response);
            }
            return;
        }
        throw new IllegalArgumentException();
    }
    
}
