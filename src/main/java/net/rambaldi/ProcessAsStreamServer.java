package net.rambaldi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Wraps a Process to provide a StreamServer.
 */
final class ProcessAsStreamServer
    implements StreamServer
{
    private final ProcessFactory factory;
    private boolean up;
    private Process process;

    public ProcessAsStreamServer(ProcessFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean isUp() {
        return up;
    }

    @Override
    public void start() throws ProcessCreationException {
        process = factory.newInstance();
        up = true;
    }

    @Override
    public void stop() {
        process.destroy();
        up = false;
    }

    @Override
    public OutputStream getInput() {
        return null;
    }

    @Override
    public InputStream getOutput() {
        return null;
    }
}
