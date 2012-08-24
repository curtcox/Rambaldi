package net.rambaldi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Curt
 */
public final class Data {

    public static byte[] readBytes(InputStream in) throws IOException {
        DataInputStream data = new DataInputStream(in);
        byte[] bytes = new byte[data.readInt()];
        data.readFully(bytes);
        return bytes;
    }
 
    public static void write(OutputStream out, byte[] bytes) throws IOException {
        DataOutputStream data = new DataOutputStream(out);
        data.writeInt(bytes.length);
        data.write(bytes);
    }

    public static void write(OutputStream out, Timestamp timestamp) throws IOException {
        DataOutputStream data = new DataOutputStream(out);
        data.writeLong(timestamp.millis);
    }

    public static void write(OutputStream out, Transaction transaction) throws IOException {
        write(out,IO.serialize(transaction));
    }

    public static Timestamp readTimestamp(InputStream in) throws IOException {
        DataInputStream data = new DataInputStream(in);
        return new Timestamp(data.readLong());
    }

    public static Transaction readTransaction(InputStream in) throws IOException {
        return (Transaction) IO.deserialize(readBytes(in));
    }

    public static Transaction readTransaction(byte[] bytes) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
