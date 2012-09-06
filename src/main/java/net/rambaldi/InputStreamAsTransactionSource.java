package net.rambaldi;

import java.io.IOException;
import java.io.InputStream;
import static java.util.Objects.*;
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
        this.in = requireNonNull(in);
        this.io = requireNonNull(io);
    }

    @Override
    public Transaction take() {
        try {
            System.err.println("Taking...");
            return io.readTransaction(in);
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }
    
}
