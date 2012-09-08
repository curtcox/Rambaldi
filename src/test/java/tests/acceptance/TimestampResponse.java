package tests.acceptance;

import net.rambaldi.process.Request;
import net.rambaldi.process.Response;
import net.rambaldi.process.Timestamp;

/**
 *
 * @author Curt
 */
final class TimestampResponse
    extends Response
{
    final Timestamp previousRequest;
    
    TimestampResponse(Request request, Timestamp previousRequest) {
        super(request.value,request);
        this.previousRequest = previousRequest;
    }
}
