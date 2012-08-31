package net.rambaldi;

/**
 * Something that can be sent requests.  A web server, a database, an email server, etc..
 * @author Curt
 */
public interface Entity {

    void queue(Request request);
}
