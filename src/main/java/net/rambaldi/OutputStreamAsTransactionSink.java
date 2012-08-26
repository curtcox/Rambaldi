package net.rambaldi;

import java.io.IOException;
import java.io.OutputStream;

/**
 * For using an OutputStream as a TransactionSink.
 * @author Curt
 */
public final class OutputStreamAsTransactionSink
    implements TransactionSink
{
    private final OutputStream out;
    private final IO io;
    
    public OutputStreamAsTransactionSink(OutputStream out, IO io) {
        this.out = out;
        this.io = io;
    }

    @Override
    public void put(Transaction transaction) {
        try {
            io.write(out,Check.notNull(transaction));
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
    
}
