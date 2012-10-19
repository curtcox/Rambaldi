package net.rambaldi;

import net.rambaldi.http.HttpRequest;
import net.rambaldi.http.HttpRequestProcessor;
import net.rambaldi.http.HttpResponse;
import net.rambaldi.process.Context;

/**
 * A processor that returns the requested file.
 * @author Curt
 */
public class HttpFileRequestProcessor
    implements HttpRequestProcessor
{

    @Override
    public HttpResponse process(HttpRequest request, Context context) {
          return null;
//        return HttpResponse.builder()
//                .request(request)
//                .content(content(request))
//                .build();
    }

}
