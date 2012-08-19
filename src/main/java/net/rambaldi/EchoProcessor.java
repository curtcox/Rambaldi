package net.rambaldi;

/**
 * A processor that echos the given request.
 * @author Curt
 */
public class EchoProcessor 
    implements RequestProcessor, java.io.Serializable
{

    @Override
    public Response process(Request request, Context context) {
        return new Response(request.bytes,request);
    }
    
}
