package tests.acceptance;

import net.rambaldi.process.Context;
import net.rambaldi.process.Timestamp;
import net.rambaldi.process.Request;
import net.rambaldi.process.RequestProcessor;

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
