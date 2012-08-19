package net.rambaldi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import tests.acceptance.Copier;

public class RequestTest {

    @Test
    public void is_serializable() throws Exception {
        Request transaction = new Request("",new Timestamp(1));
        Request copy = Copier.copy(transaction);

        assertEquals(transaction,copy);
    }

    @Test
    public void bytes_is_set_from_constructor_string() {
        String expected = "jello";
        String actual = new String(new Request(expected,new Timestamp(1)).bytes);
        assertEquals(expected,actual);
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

}
