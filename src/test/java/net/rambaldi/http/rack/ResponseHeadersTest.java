package net.rambaldi.http.rack;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

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

    @Test
    public void toString_shows_values() {
        String key = "key";
        String value = "Capital of Florida";
        String string = newInstance(key,value).toString();
        assertTrue(string,string.contains(value));
    }

    @Test
    public void toString_maps_keys_to_values() {
        String key = "color";
        String value = "red";
        String actual = newInstance(key,value).toString();
        String expected = quote("'color'='red'");
        assertEquals(expected, actual);
    }

    String quote(String s) {
        return s.replace("'","\"");
    }

    @Test
    public void size_is_0_when_headers_is_empty() {
        ResponseHeaders headers = new ResponseHeaders();
        assertTrue(headers.isEmpty());
        assertEquals(0, headers.size());
    }

    @Test
    public void size_is_1_when_headers_contains_1_item() {
        ResponseHeaders headers = newInstance("key","value");
        assertFalse(headers.isEmpty());
        assertEquals(1,headers.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void put_is_forbidden() {
        new ResponseHeaders().put("","");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void clear_is_forbidden() {
        new ResponseHeaders().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove_is_forbidden() {
        new ResponseHeaders().remove("");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void putAll_is_forbidden() {
        new ResponseHeaders().putAll(new HashMap());
    }

    @Test
    public void keySet_returns_keys() {
        Set<String> keys = newInstance("key","value").keySet();
        assertEquals(Collections.singleton("key"),keys);
    }

    @Test
    public void values_returns_values() {
        Collection<String> values = newInstance("key","value").values();
        assertEquals(Collections.singleton("value"),values);
    }

}
