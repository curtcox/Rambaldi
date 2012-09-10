package net.rambaldi.http;

import net.rambaldi.process.*;
import net.rambaldi.process.Response;
import net.rambaldi.process.Context;

/**
 * A processor that echos the given request.
 * @author Curt
 */
public class HttpEchoProcessor
    implements RequestProcessor {

    @Override
    public Response process(Request request, Context context) {
        return new Response(request.value,request);
    }
    
}
