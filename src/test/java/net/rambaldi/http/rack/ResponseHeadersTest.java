package net.rambaldi.http.rack;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ResponseHeadersTest {

    @Test
    public void header_is_a_map() {
         assertTrue(new ResponseHeaders() instanceof Map);
    }

    @Test
    public void header_must_not_contain_a_status_key() {
        assertBadKey("status");
        assertBadKey("Status");
    }

    @Test
    public void header_must_not_contain_keys_with_colons() {
        assertBadKey("a:");
    }

    @Test
    public void header_must_not_contain_keys_with_new_lines() {
        assertBadKey("a\r");
        assertBadKey("a\n");
    }

    @Test
    public void header_must_not_contain_keys_that_end_in_dash() {
        assertBadKey("a-");
    }

    @Test
    public void header_must_not_contain_keys_that_end_in_underscore() {
        assertBadKey("a_");
    }

    @Test
    public void header_keys_must_only_contain_letters_digits_underscore_and_hyphen() {
        assertBadKey("a!");
        assertBadKey("a(");
        assertGoodKey("a");
        assertGoodKey("b");
    }

    @Test
    public void header_keys_must_start_with_a_letter() {
        assertBadKey("9");
        assertBadKey("1");
        assertBadKey("");
        assertBadKey(" ");
        assertGoodKey("z");
    }

    @Test(expected = IllegalArgumentException.class)
    public void header_keys_must_be_strings() {
        newInstance(0,"string");
    }

    @Test(expected = IllegalArgumentException.class)
    public void header_values_must_be_strings() {
        newInstance("string",0);
    }

    void assertBadKey(Object key) {
        try {
            newInstance(key,"value");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains(key + " is not valid."));
        }
    }

    void assertGoodKey(String key) {
        newInstance(key,"value");
    }

    ResponseHeaders newInstance(Object key, Object value) {
        Map map = new HashMap();
        map.put(key,value);
        return new ResponseHeaders(map);
    }

    @Test
    public void toString_shows_keys() {
        String key = "Florida";
        String string = newInstance(key,"").toString();
        assertTrue(string,string.contains(key));
    }
}
