package net.rambaldi;

import static org.junit.Assert.*;
import org.junit.Test;
import tests.acceptance.Copier;

public class ResponseTest {

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

}
