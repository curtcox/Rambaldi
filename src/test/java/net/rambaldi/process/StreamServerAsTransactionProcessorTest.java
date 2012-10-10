package net.rambaldi.process;

import net.rambaldi.http.FakeStreamServer;
import net.rambaldi.Log.FakeLog;
import net.rambaldi.time.Timestamp;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class StreamServerAsTransactionProcessorTest {

    IO io = new SimpleIO();
    FakeLog log = new FakeLog();
    FakeStreamServer server = new FakeStreamServer();
    StreamServerAsTransactionProcessor processor;

    @Before
    public void before() {
        //io = new DebugIO(io,System.err);
        processor = new StreamServerAsTransactionProcessor(server,io,log);
    }

    @Test
    public void can_create() {
        new StreamServerAsTransactionProcessor(new FakeStreamServer(),io,log);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_stream_server() {
        new StreamServerAsTransactionProcessor(null,io,log);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_io() {
        new StreamServerAsTransactionProcessor(server,null,log);
    }

    @Test
    public void process_writes_request() throws Exception {
        Request request = new Request("",new Timestamp(0));

        try {
            processor.process(request);
            fail("There is nothing to read");
        } catch (DeserializationException e) {}
        Request writtenRequest = requestThatWasWritten();
        assertEquals(request, writtenRequest);
    }

    private Request requestThatWasWritten() {
        byte[] bytes = server.input.toByteArray();
        assertTrue(bytes.length>0);
        ByteArrayInputStream writtenBytes = new ByteArrayInputStream(bytes);
        InputStreamAsTransactionSource source = new InputStreamAsTransactionSource(writtenBytes,io);
        return (Request) source.take();
    }

    @Test
    public void process_returns_response_from_server() throws Exception {
        Request request = new Request("",new Timestamp(0));
        Response expected = new Response("",request);
        server.output = responseFromServer(expected);
        processor = new StreamServerAsTransactionProcessor(server,io,log);

        Response response = processor.process(request);

        assertEquals(expected, response);
    }

    @Test
    public void process_logs_exceptions() throws Exception {
        Request request = new Request("",new Timestamp(0));
        Response expected = new Response("",request);
        server.output = responseFromServer(expected);

        FakeLog log = new FakeLog();
        FakeIO io = new FakeIO() {
            @Override public void write(OutputStream out, Transaction transaction) { throw new NullPointerException();};
        };
        processor = new StreamServerAsTransactionProcessor(server,io,log);

        try {
            processor.process(request);
            fail("Request is not serializable");
        } catch (Exception e) {
            assertEquals(e,log.exception);
        }
    }

    private InputStream responseFromServer(final Response response) {
        TransactionSource source = new TransactionSource() {
            @Override
            public Transaction take() {
                return response;
            }
        };
        return new InputStreamFromTransactionSource(source,io);
    }

}
