package net.rambaldi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * An InputStream, for streaming a TransactionSource as bytes.
 * @author Curt
 */
final class InputStreamFromTransactionSource
    extends InputStream
{
    private final TransactionSource source;
    private final LinkedList<Byte> bytes = new LinkedList<>();
    private final IO io;

    public InputStreamFromTransactionSource(TransactionSource source, IO io) {
        this.source = Check.notNull(source);
        this.io = Check.notNull(io);
    }
    
    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     * <p> A subclass must provide an implementation of this method.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        if (bytes.isEmpty()) {
            take();
        }
        if (bytes.isEmpty()) {
            return -1;
        }
        return unsigned(bytes.removeFirst());
    }
    
    private void take() throws IOException {
        Transaction transaction = source.take();
        if (transaction!=null) {
            addToStream(transaction);
        }
    }
    
    private void addToStream(Transaction transaction) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        io.write(out, transaction);
        for (byte b : out.toByteArray()) {
            bytes.addLast(b);
        }
    }

    private int unsigned(byte b) {
        return b & 0xff;
    }

}
