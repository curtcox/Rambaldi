package net.rambaldi;

import java.io.Serializable;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class IOTest {

    @Test
    public void string() {
        copy("value");
    }

    void copy(Serializable original) {
        Object copy = IO.deserialize(IO.serialize(original));
        assertEquals(original,copy);
    }
}
