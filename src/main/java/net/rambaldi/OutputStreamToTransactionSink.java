package net.rambaldi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An OutputStream that puts to a TransactionSink
 * @author Curt
 */
final class OutputStreamToTransactionSink
    extends OutputStream
{

    private final TransactionSink sink;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final IO io;

    public OutputStreamToTransactionSink(TransactionSink sink, IO io) {
        this.sink = Check.notNull(sink);
        this.io = io;
    }
 
    @Override
    public void write(int b) throws IOException {
        out.write(b);
        if (isTransactionComplete()) {
            sink.put(read());
        }
    }

    public boolean isTransactionComplete() {
        try {
            read();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private Transaction read() throws IOException {
        try {
            return (Transaction) io.readTransaction(out.toByteArray());
        } catch (ClassNotFoundException e) {
            throw new DeserializationException(e);
        }
    }

}
