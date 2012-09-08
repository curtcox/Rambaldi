package net.rambaldi.process;

import java.util.Date;
import static org.junit.Assert.*;

import net.rambaldi.process.Timestamp;
import org.junit.Test;
import tests.acceptance.Copier;

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
    
    @Test
    public void is_serializable() throws Exception {
        Timestamp stamp = new Timestamp(86);
        Timestamp copy = Copier.copy(stamp);
        assertEquals(stamp,copy);
        
        assertFalse(stamp.equals(Copier.copy(new Timestamp(7))));
    }
    
    @Test
    public void toString_contains_year() {
        Timestamp timestamp = new Timestamp(0);
        int year = new Date(timestamp.millis).getYear() + 1900;
        
        assertTrue(timestamp.toString().contains(Integer.toString(year)));
    }
}
