package net.rambaldi;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class TimestampTest {

    @Test
    public void millis_uses_value_from_constructor() {
        long millis = 42;
        Timestamp stamp = new Timestamp(millis);
        assertEquals(millis,stamp.millis);
    }
}
