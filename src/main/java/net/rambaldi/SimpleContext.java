package net.rambaldi;

import java.io.Serializable;

/**
 *
 * @author Curt
 */
public final class SimpleContext
    implements Context, Serializable
{

    private Serializable state;
    
    @Override
    public Entity getEntity(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Serializable getState() {
        return state;
    }

    @Override
    public void setState(Serializable state) {
        this.state = state;
    }
    
}
