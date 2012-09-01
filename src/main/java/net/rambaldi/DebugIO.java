package net.rambaldi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * For serializing and deserializing serializable objects. 
 * @author Curt
 */
final class DebugIO
    implements IO
{

    private final IO io;
    
    public DebugIO(IO io) {
        this.io = io;
    }
    
    @Override
    public byte[] serialize(Serializable serializable) {
        byte[] result = io.serialize(serializable);
        print("serialize(%1s):%2s",serializable,result);
        return result;
    }

    @Override
    public Serializable deserialize(byte[] bytes) {
        Serializable result = deserialize(bytes);
        print("deserialize(%1s):%2s",bytes,result);
        return result;
    }

    @Override
    public Timestamp readTimestamp(InputStream in) throws IOException {
        Timestamp result = io.readTimestamp(in);
        print("readTimestamp(%1s):%2s",in,result);
        return result;
    }

    @Override
    public byte[] readBytes(InputStream in) throws IOException {
        byte[] result = io.readBytes(in);
        print("readBytes(%1s):%2s",in,result);
        return result;
    }

    @Override
    public void write(OutputStream out, Transaction transaction) throws IOException {
        print("write(out=%1s,transaction=%1s)",out,transaction);
        io.write(out, transaction);
    }

    @Override
    public void write(OutputStream out, Timestamp timestamp) throws IOException {
        print("write(out=%1s,timestamp=%2s)",out,timestamp);
        io.write(out, timestamp);
    }

    @Override
    public void write(OutputStream out, byte[] bytes) throws IOException {
        print("write(out=%1s,bytes=%2s)",out,bytes);
        io.write(out, bytes);
    }

    @Override
    public Transaction readTransaction(byte[] bytes) throws IOException, ClassNotFoundException {
        Transaction result = io.readTransaction(bytes);
        print("readTransaction(%1s):%2s",bytes,result);
        return result;
    }

    @Override
    public Transaction readTransaction(InputStream in) throws IOException {
        Transaction result = io.readTransaction(in);
        print("readTransaction(%1s):%2s",in,result);
        return result;
    }

    private void print(String template, Object... args) {
        System.out.println(String.format(template, args));
    }

}
