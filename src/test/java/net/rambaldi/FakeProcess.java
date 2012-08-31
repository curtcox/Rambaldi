package net.rambaldi;

import java.io.InputStream;
import java.io.OutputStream;

final class FakeProcess extends Process {

    InputStream in;
    InputStream err;
    OutputStream out;
    int value;
    boolean hasExited;

    @Override public OutputStream getOutputStream() { return out; }
    @Override public InputStream   getInputStream() { return in; }
    @Override public InputStream   getErrorStream() { return err; }

    @Override public int waitFor() throws InterruptedException { return value; }
    @Override public int exitValue() {
        if (hasExited)
            return value;
        throw new IllegalThreadStateException();
    }
    @Override public void destroy()  {
        hasExited = true;
    }
}
