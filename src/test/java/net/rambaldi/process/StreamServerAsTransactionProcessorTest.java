package net.rambaldi.process;

import net.rambaldi.http.FakeStreamServer;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class StreamServerAsTransactionProcessorTest {

    IO io = new SimpleIO();
    FakeStreamServer server = new FakeStreamServer();
    StreamServerAsTransactionProcessor processor;

    @Before
    public void before() {
        io = new DebugIO(io,System.err);
        processor = new StreamServerAsTransactionProcessor(server,io);
    }

    @Test
    public void can_create() {
        new StreamServerAsTransactionProcessor(new FakeStreamServer(),io);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_stream_server() {
        new StreamServerAsTransactionProcessor(null,io);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_io() {
        new StreamServerAsTransactionProcessor(server,null);
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

        Response response = processor.process(request);

        assertEquals(request,response.request);
    }

    @Test
    public void process_request_produces_response_from_processor() throws Exception {
        Request request = new Request("",new Timestamp(0));

        Response response = processor.process(request);

        assertEquals(request,response.request);
    }


    //    @Test
//    public void Read_from_standard_in_and_write_to_standard_out() throws Exception {
//        Request request = request();
//
//        StreamServer server = TransactionProcessors.newExternal(state);
//        assertFalse(server.isUp());
//
//        server.start();
//        assertTrue(server.isUp());
//
//        TransactionSink sink = new OutputStreamAsTransactionSink(server.getInput(),io);
//        TransactionSource source = new InputStreamAsTransactionSource(server.getOutput(),io);
//
//        sink.put(request);
//        server.getInput().flush();
//        Thread.sleep(1000);
//
//        Response response = (Response) source.take();
//        assertEquals(request,response.request);
//
//        server.stop();
//        assertFalse(server.isUp());
//    }

}
