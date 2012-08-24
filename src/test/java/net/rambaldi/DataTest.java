package net.rambaldi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static junit.framework.Assert.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public final class DataTest {
       
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
        Data.write(out,bytes);
        byte[] copy = Data.readBytes(new ByteArrayInputStream(out.toByteArray()));
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
        Data.write(out,timestamp);
        Timestamp copy = Data.readTimestamp(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(timestamp,copy);
    }
    
    @Test
    public void readTransaction_Request() throws IOException {
        Request expected = new Request("", new Timestamp(0));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Data.write(out, expected);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        Request actual = (Request) Data.readTransaction(in);
        
        assertEquals(expected,actual);
    }
}
