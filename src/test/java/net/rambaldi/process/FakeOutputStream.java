package net.rambaldi.process;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FakeOutputStream
    extends ByteArrayOutputStream
{
    public boolean closed;

    @Override
    public void close() throws IOException {
        closed = true;
    }
}
