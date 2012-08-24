package net.rambaldi;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Curt
 */
public final class InputTransactionSource
    implements TransactionSource
{

    private final InputStream in;
    
    public InputTransactionSource(InputStream in) {
        this.in = in;
    }

    @Override
    public Transaction take() {
        try {
            return Data.readTransaction(in);
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }
    
}
