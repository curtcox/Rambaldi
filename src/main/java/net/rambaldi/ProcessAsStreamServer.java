package net.rambaldi;

import java.io.InputStream;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

/**
 * Wraps a Process to provide a StreamServer.
 */
final class ProcessAsStreamServer
    implements StreamServer
{
    private final ProcessFactory factory;
    private final OutputStream err;
    private boolean up;
    private Process process;

    public ProcessAsStreamServer(ProcessFactory factory, OutputStream err) {
        this.factory = requireNonNull(factory);
        this.err = requireNonNull(err);
    }

    @Override
    public boolean isUp() {
        return up;
    }

    @Override
    public void start() throws ProcessCreationException {
        if (up) {
            throw new IllegalStateException("Process already started");
        }
        process = factory.newInstance();
        new StreamCopierThread(process.getErrorStream(), err).start();
        up = true;
    }

    @Override
    public void stop() {
        makeSureStarted();
        process.destroy();
        up = false;
    }

    @Override
    public OutputStream getInput() {
        makeSureStarted();
        return process.getOutputStream();
    }

    @Override
    public InputStream getOutput() {
        makeSureStarted();
        return process.getInputStream();
    }

    private void makeSureStarted() {
        if (!up) {
            throw new IllegalStateException("Process has not been started");
        }
    }

}
