package net.rambaldi.process;

import java.io.ByteArrayInputStream;

public class FakeInputStream extends ByteArrayInputStream {

    public boolean closed;

    public FakeInputStream(byte[] buf) {
        super(buf);
    }

    @Override
    public void close() {
        closed = true;
    }
}
