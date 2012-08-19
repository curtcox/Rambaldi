package net.rambaldi;

import static org.junit.Assert.*;
import org.junit.Test;
import tests.acceptance.Copier;

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

    @Test
    public void is_serializable() throws Exception {
        SimpleContext context = new SimpleContext();
        SimpleContext copy = Copier.copy(context);
        assertTrue(copy instanceof SimpleContext);
    }
}
