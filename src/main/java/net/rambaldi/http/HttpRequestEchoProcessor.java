package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.process.Request;
import net.rambaldi.process.RequestProcessor;
import net.rambaldi.process.Response;

/**
 * A processor that echos the given request.
 * @author Curt
 */
public class HttpRequestEchoProcessor
    implements HttpRequestProcessor
{

    @Override
    public HttpResponse process(HttpRequest request, Context context) {
        return HttpResponse.builder()
                .request(request)
                .content(content(request))
                .build();
    }

    private String content(HttpRequest request) {
        return "<html>" +
                     "<pre>" +
                         request +
                     "</pre>" +
                "</html>";
    }
}
