package net.rambaldi;

/**
 * Something that processes responses to requests that were previously made.
 * @author Curt
 */
public interface ResponseProcessor {
    
    void process(Response response, Request request,Context context);
}
