package net.rambaldi.process;

import java.io.Serializable;

/**
 * A simple implementation of Context.
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