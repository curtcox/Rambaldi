package net.rambaldi;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ProcessAsStreamServerTest {

    FakeProcess process = new FakeProcess();
    boolean newInstance;
    boolean processCreationFails;
    ProcessFactory factory = new ProcessFactory() {
        @Override
        public Process newInstance() throws ProcessCreationException {
            if (processCreationFails) {
                throw new ProcessCreationException(null);
            }
            newInstance = true;
            return process;
        }
    };

    @Test
    public void isUp_returns_false_initially() {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        assertFalse(server.isUp());
    }

    @Test
    public void start_returns_without_exception_when_server_start_ok() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        server.start();
    }

    @Test(expected = ProcessCreationException.class)
    public void start_throws_exception_when_server_start_fails() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        processCreationFails = true;
        server.start();
    }

    @Test
    public void isUp_returns_true_after_server_started() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        server.start();
        assertTrue(newInstance);
        assertTrue(server.isUp());
    }

    @Test
    public void isUp_returns_false_after_failed_server_start() throws Exception {
        processCreationFails = true;
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        try {
            server.start();
            fail();
        } catch (ProcessCreationException e) {
            assertFalse(newInstance);
            assertFalse(server.isUp());
        }
    }

    @Test
    public void isUp_returns_false_after_server_stopped() throws Exception{
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        server.start();
        server.stop();
        assertTrue(process.hasExited);
        assertFalse(server.isUp());
    }

    @Test
    public void getInput_delegates_to_process() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        server.start();

        assertSame(process.out,server.getInput());
    }

    @Test
    public void getOutput_delegates_to_process() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        server.start();

        assertSame(process.in,server.getOutput());
    }

    @Test(expected=IllegalStateException.class)
    public void getInput_throws_exception_when_process_not_started() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        server.getInput();
    }

    @Test(expected = IllegalStateException.class)
    public void getOutput_throws_exception_when_process_not_started() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        server.getOutput();
    }

}
