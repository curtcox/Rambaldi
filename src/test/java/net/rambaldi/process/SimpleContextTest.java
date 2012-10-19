package net.rambaldi.process;

import java.io.Serializable;
import static org.junit.Assert.*;

import net.rambaldi.process.SimpleContext;
import org.junit.Test;
import tests.acceptance.Copier;

/**
 * A simple implementation of Context.
 * @author Curt
 */
public class SimpleContextTest {

    @Test
    public void uses_filesystem_from_constructor() {
        FileSystem expected = new SimpleFileSystem(null);
        SimpleContext context = new SimpleContext(expected);
        FileSystem actual = context.getFileSystem();
        assertSame(expected,actual);
    }

    @Test(expected = NullPointerException.class)
    public void filesystem_is_required() {
        new SimpleContext(null);
    }

    @Test
    public void getState_returns_previously_setState() {
        SimpleContext context = new SimpleContext();
        
        Serializable state = new Serializable() {};
        
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
