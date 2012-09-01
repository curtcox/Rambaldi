package net.rambaldi;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A TransactionProcessor that uses streams for IO.
 * @author Curt
 */
public final class StreamTransactionProcessor
    implements TransactionProcessor, java.io.Serializable
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
        this.in = new InputStreamAsTransactionSource(in,io);
        this.out = new OutputStreamAsTransactionSink(out,io);
        this.err = new OutputStreamAsTransactionSink(err,io);
        this.context = context;
        this.requests = requests;
        this.responses = responses;
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
