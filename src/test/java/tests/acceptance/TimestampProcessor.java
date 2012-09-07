package tests.acceptance;

import net.rambaldi.Context;
import net.rambaldi.Request;
import net.rambaldi.RequestProcessor;
import net.rambaldi.Timestamp;

/**
 * Returns a response with the previous (potentially null) timestamp.
 * This artificial processor just exists as a RequestProcessor that uses context to store state.
 */
final class TimestampProcessor
    implements RequestProcessor, java.io.Serializable
{

    @Override
    public TimestampResponse process(Request request, Context context) {
        Timestamp previous = (Timestamp) context.getState();
        context.setState(request.getTimestamp());
        return new TimestampResponse(request,previous);
    }
    
}
