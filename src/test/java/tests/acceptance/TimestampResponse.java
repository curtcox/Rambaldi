package tests.acceptance;

import net.rambaldi.Request;
import net.rambaldi.Response;
import net.rambaldi.Timestamp;

/**
 *
 * @author Curt
 */
final class TimestampResponse
    extends Response
{
    final Timestamp previousRequest;
    
    TimestampResponse(Request request, Timestamp previousRequest) {
        super(request);
        this.previousRequest = previousRequest;
    }
}
