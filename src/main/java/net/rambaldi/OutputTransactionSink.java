package net.rambaldi;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Curt
 */
public final class OutputTransactionSink
    implements TransactionSink
{

    private final OutputStream out;
    
    public OutputTransactionSink(OutputStream out) {
        this.out = out;
    }

    @Override
    public void put(Transaction transaction) {
        try {
            Data.write(out,transaction);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
    
}
