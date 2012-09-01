package net.rambaldi;

/**
 * A processor that echos the given request.
 * @author Curt
 */
public class EchoProcessor 
    implements RequestProcessor
{

    @Override
    public Response process(Request request, Context context) {
        return new Response(request.value,request);
    }
    
}
