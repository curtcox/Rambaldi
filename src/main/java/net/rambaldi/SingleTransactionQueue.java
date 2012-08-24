package net.rambaldi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides multiple ways of connecting to a processor.
 * @author Curt
 */
public class SingleTransactionQueue
    implements TransactionSource, TransactionSink, java.io.Serializable
{
    private Transaction transaction;
    private boolean empty = true;
    
    @Override
    public Transaction take() {
        empty = true;
        return transaction;
    }

    @Override
    public void put(Transaction transaction) {
        empty = false;
        this.transaction = transaction;
    }
 
    public boolean isEmpty() {
        return empty;
    }
    
    public InputStream asInputStream() {
        final List<Integer> bytes = new ArrayList<>();
        for (byte b : IO.serialize(take())) {
            bytes.add((int) b);
        }
        return new InputStream() {
            int i;
            @Override
            public int read() throws IOException {
                return bytes.get(i++);
            }
        };
    }
    
    public OutputStream asOutputStream() {
        return new OutputStream() {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            @Override
            public void write(int b) throws IOException {
                out.write(b);
                if (lastByteInTransaction(b)) {
                    put();
                }
            }
            
            boolean lastByteInTransaction(int b) {
                byte[] bytes = out.toByteArray();
                try {
                    IO.deserialize(bytes);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            
            void put() {
                Transaction transaction = (Transaction) IO.deserialize(out.toByteArray());
                SingleTransactionQueue.this.put(transaction);
            }
        };
    }
}
