package net.rambaldi;

/**
 * The simplest implementation of TransactionProcessor
 * @author Curt
 */
public final class SimpleTransactionProcessor
    implements TransactionProcessor, java.io.Serializable
{

    public final TransactionSource in;
    public final TransactionSink out;
    public final TransactionSink err;
    public final Context context;
    public final RequestProcessor requests;
    public final ResponseProcessor responses;

    public SimpleTransactionProcessor(TransactionSource in, TransactionSink out, TransactionSink err,
            Context context, RequestProcessor requests, ResponseProcessor responses)
    {
        this.in = in;
        this.out = out;
        this.err = err;
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
