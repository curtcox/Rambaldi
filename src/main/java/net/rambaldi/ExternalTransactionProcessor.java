package net.rambaldi;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A TransactionProcessor that uses streams for IO.
 * @author Curt
 */
public class ExternalTransactionProcessor
    implements TransactionProcessor, java.io.Serializable
{
    public final TransactionSource in;
    public final TransactionSink out;
    public final TransactionSink err;
    public final Process process;

    public ExternalTransactionProcessor(
            InputStream in, OutputStream out, OutputStream err, IO io, Process process)
    {
        this.in = new InputStreamAsTransactionSource(in,io);
        this.out = new OutputStreamAsTransactionSink(out,io);
        this.err = new OutputStreamAsTransactionSink(err,io);
        this.process = process;
    }

    @Override
    public void process() {
        throw new UnsupportedOperationException();
    }
    
}
