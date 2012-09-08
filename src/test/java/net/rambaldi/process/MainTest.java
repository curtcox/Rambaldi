package net.rambaldi.process;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Acceptance tests for launching an external processor.
 * @author Curt
 */
public class MainTest {

    IO io = new SimpleIO();
    Path temp = Paths.get("tempDir");
    StateOnDisk state;
    ProcessFactory processFactory;
    ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    Main main;
    FakeProcess process = new FakeProcess();

    @Before
    public void Before() throws Exception {
        //io = new DebugIO(io,System.out);
        state = new StateOnDisk(temp,io,new FakeFileSystem());
        state.setProcessor(new EchoProcessor());
        state.persist();

        processFactory = new ProcessFactory() {
            @Override
            public Process newInstance() throws ProcessCreationException {
                return process;
            }
        };
    }

    @After
    public void After() throws Exception {
        state.delete();
    }

    @Test
    public void constructor_works_with_non_nulls_args() {
        new Main(io,state,in,out,err);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_io() {
        new Main(null,state,in,out,err);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_state() {
        new Main(io,null,in,out,err);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_in() {
        new Main(io,state,null,out,err);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_out() {
        new Main(io,state,in,null,err);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_err() {
        new Main(io,state,in,out,null);
    }

    @Test
    public void Read_will_throw_an_Exception_when_input_throws_EOFException() throws Exception {
        in = new ByteArrayInputStream(new byte[0]);
        main = new Main(io,state,in,out,err);
        try {
           main.run();
        } catch (Exception e) {
           assertTrue(e.getCause() instanceof EOFException);
        }
    }

    @Test
    public void Read_from_standard_in_and_write_to_standard_out() throws Exception {
        Request request = request();

        SingleTransactionQueue inputToMain = new SingleTransactionQueue(io);
        SingleTransactionQueue outputFromMain = new SingleTransactionQueue(io);
        inputToMain.put(request);
        main = new Main(io,state,inputToMain.asInputStream(),outputFromMain.asOutputStream(),err);
        try {
            main.run();
            fail();
        } catch (DeserializationException e) {

        }
        Response response = (Response) outputFromMain.take();
        assertEquals(request,response.request);
    }

    private Request request() {
        return new Request("",new Timestamp(0));
    }

}
