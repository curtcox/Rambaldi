package net.rambaldi.process;

import static org.junit.Assert.*;

import net.rambaldi.time.Timestamp;
import org.junit.Test;
import tests.acceptance.Copier;

public class ResponseTest {

    final SimpleIO io = new SimpleIO();

    @Test
    public void is_serializable() throws Exception {
        Response transaction = new Response("",new Request("",new Timestamp(1)));
        Response copy = Copier.copy(transaction);

        assertEquals(transaction,copy);
    }

    @Test
    public void equals_returns_true_when_responses_are_equal() throws Exception {
        Response response1 = new Response("",new Request("same",new Timestamp(1)));
        Response response2 = new Response("",new Request("same",new Timestamp(1)));

        assertEquals(response1,response2);
    }

    @Test
    public void equals_returns_false_when_responses_are_not_equal() throws Exception {
        Response response1 = new Response("",new Request("same",new Timestamp(1)));
        Response response2 = new Response("",new Request("different",new Timestamp(1)));

        assertFalse(response1.equals(response2));
    }

    @Test
    public void serialization_returns_equivalent_response() {
        Request   request = new Request("",new Timestamp(0));
        Response expected = new Response("",request);
        Response   actual = (Response) io.deserialize(io.serialize(expected));
        
        assertEquals(expected,actual);
    }

}
