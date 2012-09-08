package net.rambaldi.process;

/**
 * Something that can be sent requests.  A web server, a database, an email server, etc..
 * @author Curt
 */
public interface Entity {

    /**
     * Queue a request for this entity.
     * @param request
     */
    void queue(Request request);
}
