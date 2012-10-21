package net.rambaldi.http;

import net.rambaldi.file.FakeFileSystem;
import net.rambaldi.file.SimpleRelativePath;
import net.rambaldi.process.SimpleContext;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;
import static net.rambaldi.http.HttpRequest.Method.GET;
import static org.junit.Assert.assertEquals;

public class HttpFileRequestProcessorTest {

    HttpFileRequestProcessor processor = new HttpFileRequestProcessor();
    final String content = "contents of resource";
    FakeFileSystem fileSystem = new FakeFileSystem() {
        @Override public byte[] readAllBytes(RelativePath path) throws IOException {
            if (!path.equals(new SimpleRelativePath("resource_name"))) {
                throw new UnsupportedOperationException("path=" + path);
            }
            return content.getBytes();
        }
    };
    SimpleContext context = new SimpleContext(fileSystem);

    @Test
    public void can_create() {
        new HttpFileRequestProcessor();
    }

    @Test
    public void process_returns_response() throws Exception {
        HttpRequest request = HttpRequest.builder().method(GET).resource("/resource_name").build();
        assertNotNull(processor.process(request,context));
    }

    @Test
    public void content_matches_contents_of_filesystem_path() throws Exception {
        HttpRequest request = HttpRequest.builder().method(GET).resource("/resource_name").build();
        HttpResponse response = processor.process(request,context);
        assertEquals(content,response.content);
    }

}
