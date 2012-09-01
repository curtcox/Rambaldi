package net.rambaldi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author Curt
 */
public class SimpleIOTest {

    final SimpleIO io = new SimpleIO();
    
    @Test
    public void string() {
        copy("value");
    }

    void copy(Serializable original) {
        Object copy = io.deserialize(io.serialize(original));
        assertEquals(original,copy);
    }
    
        @Test
    public void empty() throws IOException {
        equal(new byte[0]);
    }

    @Test
    public void byte_1() throws IOException {
        equal(new byte[] {1});
    }

    @Test
    public void byte_2() throws IOException {
        equal(new byte[]{0,1});
    }

    @Test
    public void byte_2222() throws IOException {
        byte[] bytes = new byte[2222];
        for (int i=0; i<2222; i++) {
            bytes[i] = (byte) (i % 255);
        }
        equal(bytes);
    }

    void equal(byte[] bytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        io.write(out,bytes);
        byte[] copy = io.readBytes(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(bytes.length,copy.length);
        for (int i=0; i<bytes.length; i++) {
            assertEquals(bytes[i],copy[i]);
        }
    }
    
    @Test
    public void timestamp_0() throws IOException {
        equal(new Timestamp(0));
    }

    @Test
    public void timestamp_1() throws IOException {
        equal(new Timestamp(1));
    }

    void equal(Timestamp timestamp) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        io.write(out,timestamp);
        Timestamp copy = io.readTimestamp(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(timestamp,copy);
    }
    
    @Test
    public void readTransaction_from_InputStream_Request() throws IOException {
        Request expected = new Request("", new Timestamp(0));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        io.write(out, expected);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());        
        Request actual = (Request) io.readTransaction(in);
        
        assertEquals(expected,actual);
    }
    
    @Test
    public void readTransaction_from_byte_array_Request() throws Exception {
        Request expected = new Request("", new Timestamp(0));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        io.write(out, expected);
        Request actual = (Request) io.readTransaction(out.toByteArray());
        
        assertEquals(expected,actual);
    }

}
