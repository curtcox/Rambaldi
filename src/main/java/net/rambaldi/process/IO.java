package net.rambaldi.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Something that can read and write objects, so that they can be transported.
 * @author Curt
 */
public interface IO {

    byte[] serialize(Serializable serializable);
    Serializable deserialize(byte[] bytes);

    void write(OutputStream out, Transaction transaction) throws IOException;
    void write(OutputStream out, Timestamp timestamp) throws IOException;
    void write(OutputStream out, byte[] bytes) throws IOException;

    Transaction readTransaction(byte[] bytes) throws IOException, ClassNotFoundException;
    Transaction readTransaction(InputStream in) throws IOException;
    Timestamp readTimestamp(InputStream in) throws IOException;
    byte[] readBytes(InputStream in) throws IOException;
}
