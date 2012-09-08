package net.rambaldi.process;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

public class ProcessAsStreamServerTest {

    FakeProcess process = new FakeProcess();
    ByteArrayOutputStream err = new ByteArrayOutputStream();
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

    @Test(expected = NullPointerException.class)
    public void constructor_requires_factory() {
        new ProcessAsStreamServer(null, err);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_err() {
        new ProcessAsStreamServer(factory, null);
    }

    @Test
    public void isUp_returns_false_initially() {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        assertFalse(server.isUp());
    }

    @Test
    public void start_returns_without_exception_when_server_start_ok() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.start();
    }

    @Test(expected = IllegalStateException.class)
    public void start_throws_exception_if_already_up() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.start();
        server.start();
    }

    @Test
    public void start_copies_standard_err_to_given_stream() throws Exception {
        byte[] expected = new byte[100];
        process.err = new ByteArrayInputStream(expected);
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory,err);
        server.start();
        Thread.sleep(100);
        assertBytesEqual(expected, err.toByteArray());
    }

    private void assertBytesEqual(byte[] expected, byte[] actual) {
        assertEquals(expected.length,actual.length);
        for (int i=0; i<expected.length; i++) {
            assertEquals(String.format("at[%s] %s!=%s",i,expected[i],actual[i]),expected[i],actual[i]);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void stop_throws_exception_if_not_started() {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.stop();
    }

    public void stop_exits_process() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.start();
        server.stop();
        assertTrue(process.hasExited);
    }

    @Test(expected = ProcessCreationException.class)
    public void start_throws_exception_when_server_start_fails() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        processCreationFails = true;
        server.start();
    }

    @Test
    public void isUp_returns_true_after_server_started() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.start();
        assertTrue(newInstance);
        assertTrue(server.isUp());
    }

    @Test
    public void isUp_returns_false_after_failed_server_start() throws Exception {
        processCreationFails = true;
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
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
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.start();
        server.stop();
        assertTrue(process.hasExited);
        assertFalse(server.isUp());
    }

    @Test
    public void getInput_delegates_to_process() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.start();

        assertSame(process.out,server.getInput());
    }

    @Test
    public void getOutput_delegates_to_process() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.start();

        assertSame(process.in,server.getOutput());
    }

    @Test(expected=IllegalStateException.class)
    public void getInput_throws_exception_when_process_not_started() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.getInput();
    }

    @Test(expected = IllegalStateException.class)
    public void getOutput_throws_exception_when_process_not_started() throws Exception {
        ProcessAsStreamServer server = new ProcessAsStreamServer(factory, err);
        server.getOutput();
    }

}
