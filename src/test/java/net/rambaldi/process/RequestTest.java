package net.rambaldi.process;

import static org.junit.Assert.*;

import net.rambaldi.time.Timestamp;
import org.junit.Test;
import tests.acceptance.Copier;

public class RequestTest {

    final SimpleIO io = new SimpleIO();

    @Test
    public void is_serializable() throws Exception {
        Request transaction = new Request("",new Timestamp(1));
        Request copy = Copier.copy(transaction);

        assertEquals(transaction,copy);
    }

    @Test
    public void equals_returns_true_when_requests_are_equal() throws Exception {
        Request request1 = new Request("same",new Timestamp(1));
        Request request2 = new Request("same",new Timestamp(1));

        assertEquals(request1,request2);
    }

    @Test
    public void equals_returns_false_when_requests_are_not_equal() throws Exception {
        Request request1 = new Request("same",new Timestamp(1));
        Request request2 = new Request("different",new Timestamp(1));

        assertFalse(request1.equals(request2));
    }

    @Test
    public void serialization_returns_equivalent_request() {
        Transaction expected = new Request("",new Timestamp(0));
        Transaction actual = (Transaction) io.deserialize(io.serialize(expected));
        
        assertEquals(expected,actual);
    }

    @Test
    public void toString_includes_timestamp() {
        Timestamp timestamp = new Timestamp(0);
        Request request = new Request("",timestamp);
        assertTrue(request.toString().contains(timestamp.toString()));
    }

    @Test
    public void toString_includes_value() {
        Request request = new Request("jello",new Timestamp(0));
        assertTrue(request.toString().contains("jello"));
    }

    @Test
    public void toString_includes_request_literal() {
        Request request = new Request("",new Timestamp(0));
        assertTrue(request.toString().toLowerCase().contains("request"));
    }

    @Test(expected=NullPointerException.class)
    public void constructor_throw_NPE_for_null_value() {
        new Request(null,new Timestamp(1));
    }

    @Test(expected=NullPointerException.class)
    public void constructor_throw_NPE_for_null_timestamp() {
        new Request("",null);
    }
}
