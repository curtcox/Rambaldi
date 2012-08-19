package net.rambaldi;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class SimpleContextTest {

    @Test
    public void getState_returns_previously_setState() {
        SimpleContext context = new SimpleContext();
        
        Object state = new Object();
        
        context.setState(state);
        Object result = context.getState();
        
        assertSame(state,result);
    }
}
