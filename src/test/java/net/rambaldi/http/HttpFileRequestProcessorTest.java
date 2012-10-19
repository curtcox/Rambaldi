package net.rambaldi.http;

import net.rambaldi.file.FakeFileSystem;
import net.rambaldi.process.SimpleContext;
import org.junit.Test;

import java.io.IOException;

import static net.rambaldi.http.HttpRequest.Method.GET;
import static org.junit.Assert.assertEquals;

public class HttpFileRequestProcessorTest {

    @Test
    public void can_create() {
        new HttpFileRequestProcessor();
    }

    @Test
    public void content_matches_contents_of_filesystem_path() {
        HttpFileRequestProcessor processor = new HttpFileRequestProcessor();
        HttpRequest request = HttpRequest.builder().method(GET).resource("resource_name").build();
        final String content = "contents of resource";
        FakeFileSystem fileSystem = new FakeFileSystem() {
            @Override public byte[] readAllBytes(RelativePath path) throws IOException {
                return content.getBytes();
            }
        };
        SimpleContext context = new SimpleContext(fileSystem);
        HttpResponse response = processor.process(request,context);
        assertEquals(content,response.content);
    }

}
