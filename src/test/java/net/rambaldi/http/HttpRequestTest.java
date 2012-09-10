package net.rambaldi.http;

import net.rambaldi.process.Timestamp;
import org.junit.Test;

public class HttpRequestTest {
    @Test
    public void can_create() {
        new HttpRequest("",new Timestamp(0), HttpRequest.Method.GET);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_method() {
        new HttpRequest("",new Timestamp(0), null);
    }
}
