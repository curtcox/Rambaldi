package net.rambaldi.http;

import net.rambaldi.Context;
import net.rambaldi.Request;
import net.rambaldi.RequestProcessor;
import net.rambaldi.Response;

/**
 * A processor that echos the given request.
 * @author Curt
 */
public class HTTPEchoProcessor
    implements RequestProcessor
{

    @Override
    public Response process(Request request, Context context) {
        return new Response(request.value,request);
    }
    
}
