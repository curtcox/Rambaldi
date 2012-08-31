package net.rambaldi;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessAsStreamServerTest {

    FakeProcess process = new FakeProcess();
    boolean newInstance;
    ProcessFactory factory = new ProcessFactory() {
        @Override
        public Process newInstance() {
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
    public void isUp_returns_true_after_server_started() {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        server.start();
        assertTrue(newInstance);
        assertTrue(server.isUp());
    }

    @Test
    public void isUp_returns_false_after_server_stopped() {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory);
        server.start();
        server.stop();
        assertTrue(process.hasExited);
        assertFalse(server.isUp());
    }

}
