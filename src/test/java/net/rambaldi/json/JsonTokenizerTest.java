package net.rambaldi.json;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonTokenizerTest {

    @Test(expected = NullPointerException.class)
    public void string_is_required() {
        assertTokens(json(null));
    }

    @Test
    public void no_tokens() {
        assertTokens(json(""));
    }

    @Test
    public void empty_braces() {
        assertTokens(json("{}"), "{", "}");
    }

    @Test
    public void empty_braces_with_space() {
        assertTokens(json("{ }"), "{", "}");
    }

    @Test
    public void left_brace() {
        assertTokens("{", "{");
    }

    @Test
    public void right_brace() {
        assertTokens("}", "}");
    }

    @Test
    public void colon() {
        assertTokens(":", ":");
    }

    @Test
    public void red() {
        assertTokens(json("'red'"), "red");
    }

    @Test
    public void _1_internal_space() {
        assertTokens(json("'a a'"), "a a");
    }

    @Test
    public void _2_internal_spaces() {
        assertTokens(json("'a  a'"), "a  a");
    }

    @Test
    public void _3_internal_spaces() {
        assertTokens(json("'a   a'"), "a   a");
    }

    @Test
    public void a_simple_assignment_in_braces() {
        assertTokens(json("{ 'name' : 'Fido' }"), "{", "name", ":", "Fido", "}");
    }

    @Test
    public void a_simple_assignment_with_no_braces() {
        assertTokens(json(" 'name' : 'Fido' "), "name", ":", "Fido");
    }

    @Test
    public void a_simple_assignment_with_no_braces_but_no_spaces() {
        assertTokens(json("{'name':'Fido'}"), "{", "name", ":", "Fido", "}");
    }

    private String json(String s) {
        return s.replace("'","\"");
    }

    private void assertTokens(String json, String... tokens) {
        List<String> expected = Arrays.asList(tokens);
        List<String> actual = new ArrayList<>();
        for (String token : new JsonTokenizer(json)) {
            actual.add(token);
        }
        assertEquals(expected,actual);
    }

}
