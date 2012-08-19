package net.rambaldi;

import java.io.Serializable;

/**
 * The context that a request processor processes requests in.
 * @author Curt
 */
public interface Context {
    
    /**
     * Return a named entity to queue requests or responses to.
     */
    Entity getEntity(String name);
    
    /**
     * Return the current state.
     */
    Serializable getState();
    
    /**
     * Set the current state.
     */
    void setState(Serializable state);
}
