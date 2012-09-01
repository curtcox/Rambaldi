package net.rambaldi;

/**
 * Something that processes responses to requests that were previously made.
 * @author Curt
 */
public interface ResponseProcessor {

    /**
     * Given this response to the previous request, take whatever further action is needed.
     */
    void process(Response response, Request request,Context context);
}
