package net.rambaldi;

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
    Object getState();
    
    void setState(Object state);
}
