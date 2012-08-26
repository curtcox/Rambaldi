package net.rambaldi;

import java.io.IOException;
import java.io.InputStream;

/**
 * For using an InputStream as a Transaction source.
 * @author Curt
 */
public final class InputStreamAsTransactionSource
    implements TransactionSource
{

    private final InputStream in;
    private final IO io;
    
    public InputStreamAsTransactionSource(InputStream in, IO io) {
        this.in = in;
        this.io = io;
    }

    @Override
    public Transaction take() {
        try {
            return io.readTransaction(in);
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }
    
}
