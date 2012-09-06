package net.rambaldi;

import java.io.*;

import static java.util.Objects.requireNonNull;

/**
 * For serializing and deserializing serializable objects. 
 * @author Curt
 */
public final class DebugIO
    implements IO
{

    private final IO io;
    private final PrintStream out;
    
    public DebugIO(IO io, PrintStream out) {
        this.io = requireNonNull(io);
        this.out = requireNonNull(out);
    }
    
    @Override
    public byte[] serialize(Serializable serializable) {
        byte[] result = io.serialize(serializable);
        print("serialize(%1s):%2s",serializable,result);
        return result;
    }

    @Override
    public Serializable deserialize(byte[] bytes) {
        print("deserialize(%1s)",bytes);
        Serializable result = deserialize(bytes);
        print("deserialize(%1s):%2s",bytes,result);
        return result;
    }

    @Override
    public Timestamp readTimestamp(InputStream in) throws IOException {
        print("readTimestamp(%1s)",in);
        Timestamp result = io.readTimestamp(in);
        print("readTimestamp(%1s):%2s",in,result);
        return result;
    }

    @Override
    public byte[] readBytes(InputStream in) throws IOException {
        print("readBytes(%1s)",in);
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
        print("readTransaction(%1s)",bytes);
        Transaction result = io.readTransaction(bytes);
        print("readTransaction(%1s):%2s",bytes,result);
        return result;
    }

    @Override
    public Transaction readTransaction(InputStream in) throws IOException {
        print("readTransaction(%1s)",in);
        Transaction result = io.readTransaction(in);
        print("readTransaction(%1s):%2s",in,result);
        return result;
    }

    private void print(String template, Object... args) {
        out.println(String.format(template, formatObjects(args)));
    }

    private String[] formatObjects(Object[] args) {
        String[] strings = new String[args.length];
        for (int i=0; i<args.length; i++) {
            strings[i] = formatObject(args[i]);
        }
        return strings;
    }

    private String formatObject(Object object) {
        if (object==null) {
            return null;
        }
        if (object instanceof ByteArrayInputStream) {
            object = ((ByteArrayInputStream) object).available() + " available in " + object;
        }
        if (object instanceof ByteArrayOutputStream) {
            object = ((ByteArrayOutputStream) object).toByteArray().length + " bytes in " + object;
        }
        if (object instanceof byte[]) {
            return ((byte[]) object).length + " bytes";
        }
        return object.toString();
    }
}
