package net.rambaldi;

/**
 *
 * @author Curt
 */
public final class SimpleContext
    implements Context
{

    private Object state;
    
    @Override
    public Entity getEntity(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getState() {
        return state;
    }

    @Override
    public void setState(Object state) {
        this.state = state;
    }
    
}
