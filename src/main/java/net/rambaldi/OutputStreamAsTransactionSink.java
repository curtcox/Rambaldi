package net.rambaldi;

import net.rambaldi.SerializationException;
import net.rambaldi.Transaction;
import net.rambaldi.TransactionSink;
import net.rambaldi.IO;

import java.io.IOException;
import java.io.OutputStream;
import static java.util.Objects.*;

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
        this.out = requireNonNull(out);
        this.io = requireNonNull(io);
    }

    @Override
    public void put(Transaction transaction) {
        try {
            io.write(out, requireNonNull(transaction));
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
    
}
