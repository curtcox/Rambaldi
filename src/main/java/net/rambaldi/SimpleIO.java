package net.rambaldi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import static java.util.Objects.*;

/**
 * For serializing and deserializing serializable objects. 
 * @author Curt
 */
public final class SimpleIO
    implements IO, Serializable
{

    public static final SimpleIO IO = new SimpleIO();

    @Override
    public byte[] serialize(Serializable serializable) {
        requireNonNull(serializable);
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
                out.writeObject(serializable);
            }
            return bytes.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public Serializable deserialize(byte[] bytes) {
        requireNonNull(bytes);
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (Serializable) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new DeserializationException(e);
        }
    }
    
    private DataInputStream data(InputStream in) {
        return new DataInputStream(in);
    }

    private DataOutputStream data(OutputStream out) {
        return new DataOutputStream(out);
    }

    @Override
    public Timestamp readTimestamp(InputStream in) throws IOException {
        requireNonNull(in);
        return new Timestamp(data(in).readLong());
    }

    @Override
    public byte[] readBytes(InputStream in) throws IOException {
        requireNonNull(in);
        DataInputStream data = data(in);
        byte[] bytes = new byte[data.readInt()];
        data.readFully(bytes);
        return bytes;
    }

    @Override
    public void write(OutputStream out, Transaction transaction) throws IOException {
        write(out, serialize(transaction));
    }

    @Override
    public void write(OutputStream out, Timestamp timestamp) throws IOException {
        data(out).writeLong(timestamp.millis);
    }

    @Override
    public void write(OutputStream out, byte[] bytes) throws IOException {
        requireNonNull(out);
        requireNonNull(bytes);
        DataOutputStream data = data(out);
        data.writeInt(bytes.length);
        data.write(bytes);
    }

    @Override
    public Transaction readTransaction(byte[] bytes) throws IOException, ClassNotFoundException {
        return readTransaction(new ByteArrayInputStream(requireNonNull(bytes)));
    }

    @Override
    public Transaction readTransaction(InputStream in) throws IOException {
        return (Transaction) deserialize(readBytes(requireNonNull(in)));
    }

}
