package net.rambaldi.http;

import net.rambaldi.file.FileSystem;
import net.rambaldi.file.SimpleRelativePath;
import net.rambaldi.process.Context;

import java.io.IOException;

/**
 * A processor that returns the requested file.
 * @author Curt
 */
public class HttpFileRequestProcessor
    implements HttpRequestProcessor
{

    @Override
    public HttpResponse process(HttpRequest request, Context context) throws Exception {
        return HttpResponse.builder()
                .request(request)
                .content(content(request, context))
                .build();
    }

    private String content(HttpRequest request, Context context) throws IOException {
        FileSystem.RelativePath path = new SimpleRelativePath(request.resource.name.substring(1));
        return new String(context.getFileSystem().readAllBytes(path));
    }

}
