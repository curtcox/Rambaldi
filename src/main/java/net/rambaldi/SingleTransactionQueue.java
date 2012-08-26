package net.rambaldi;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides multiple ways of connecting to a processor.
 * Null transactions are rejected.
 * @author Curt
 */
public final class SingleTransactionQueue
    implements TransactionSource, TransactionSink, java.io.Serializable
{
    private Transaction transaction;
    private final IO io;
    
    public SingleTransactionQueue(IO io) {
        this.io = io;
    }
    
    @Override
    public Transaction take() {
        Transaction taken = transaction;
        transaction = null;
        return taken;
    }

    @Override
    public void put(Transaction transaction) {
        Check.notNull(transaction);
        this.transaction = transaction;
    }
 
    public boolean isEmpty() {
        return transaction==null;
    }
    
    /**
     * Return an InputStream that Transactions can be read from as bytes.
     */
    public InputStream asInputStream() {
        return new InputStreamFromTransactionSource(this,io);
    }

    /**
     * Return an OutputStream that Transactions can be written to as bytes.
     */
    public OutputStream asOutputStream() {
        return new OutputStreamToTransactionSink(this,io);
    }
}
