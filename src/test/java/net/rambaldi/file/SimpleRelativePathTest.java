package net.rambaldi.file;

import net.rambaldi.file.SimpleRelativePath;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SimpleRelativePathTest {

    @Test
    public void can_create() {
        new SimpleRelativePath();
    }

    @Test(expected = NullPointerException.class)
    public void first_path_element_must_not_be_null() {
        new SimpleRelativePath(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void first_element_must_not_be_empty() {
        new SimpleRelativePath("");
    }

    @Test(expected = NullPointerException.class)
    public void no_path_element_can_be_null() {
        new SimpleRelativePath("a","b","c",null,"d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void no_path_element_can_be_empty() {
        new SimpleRelativePath("a","b","c","","d");
    }

    @Test
    public void elements_is_empty_when_no_constructor_args() {
        new SimpleRelativePath().elements().isEmpty();
    }

    @Test
    public void elements_matches_constructor_args() {
        String[] args = new String[] {"a","c","3"};
        List<String> actual = new SimpleRelativePath(args).elements();
        List<String> expected = Arrays.asList(args);

        assertEquals(expected,actual);
    }

    @Test
    public void two_empty_paths_are_equal() {
        SimpleRelativePath a = new SimpleRelativePath();
        SimpleRelativePath b = new SimpleRelativePath();
        assertEquals(a,b);
    }

    @Test
    public void two_empty_paths_have_the_same_hash() {
        SimpleRelativePath a = new SimpleRelativePath();
        SimpleRelativePath b = new SimpleRelativePath();
        assertEquals(a.hashCode(),b.hashCode());
    }

    @Test
    public void two_different_paths_are_not_equal() {
        SimpleRelativePath a = new SimpleRelativePath("a");
        SimpleRelativePath b = new SimpleRelativePath("b");
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void a_single_element_path_can_be_constructed_in_one_or_two_steps() {
        SimpleRelativePath a = new SimpleRelativePath("one");
        SimpleRelativePath b = new SimpleRelativePath().resolve("one");
        assertEquals(a.hashCode(),b.hashCode());
    }

}
