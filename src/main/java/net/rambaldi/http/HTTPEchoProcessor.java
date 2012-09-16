package net.rambaldi.http;

import net.rambaldi.process.*;
import net.rambaldi.process.Response;
import net.rambaldi.process.Context;

/**
 * A processor that echos the given request.
 * @author Curt
 */
public class HttpEchoProcessor
    implements HttpTransactionProcessor
{

    @Override
    public HttpResponse process(HttpRequest request) {
        return HttpResponse.builder().request(request).build();
    }
}
