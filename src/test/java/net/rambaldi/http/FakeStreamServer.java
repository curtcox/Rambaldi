package net.rambaldi.http;

import net.rambaldi.process.ProcessCreationException;
import net.rambaldi.process.StreamServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FakeStreamServer
    implements StreamServer
{
    public InputStream output = new ByteArrayInputStream(new byte[0]);
    public ByteArrayOutputStream input = new ByteArrayOutputStream();

    @Override public boolean isUp() { return false; }
    @Override public void start() throws ProcessCreationException {}
    @Override public void stop() {}
    @Override public OutputStream getInput() { return input; }
    @Override public InputStream getOutput() { return output; }
}
