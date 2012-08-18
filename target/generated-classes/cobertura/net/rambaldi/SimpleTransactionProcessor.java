package net.rambaldi;

/**
 *
 * @author Curt
 */
public class SimpleTransactionProcessor 
    implements TransactionProcessor
{

    private final TransactionSource in;
    private final TransactionSink out;
    private final TransactionSink err;
    private final Context context;
    private final RequestProcessor requests;
    private final ResponseProcessor responses;

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
