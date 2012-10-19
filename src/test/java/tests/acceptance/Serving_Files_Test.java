package tests.acceptance;

import net.rambaldi.http.HttpFileRequestProcessor;
import net.rambaldi.http.*;
import net.rambaldi.file.FileSystem;
import net.rambaldi.process.SimpleContext;
import net.rambaldi.file.SimpleFileSystem;
import net.rambaldi.file.SimpleRelativePath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static net.rambaldi.http.HttpRequest.Method.GET;
import static org.junit.Assert.assertEquals;

public class Serving_Files_Test {

    FileSystem fileSystem = new SimpleFileSystem(null);
    FileSystem.RelativePath dir = newDirectory();

    @Before
    public void before() throws IOException {
        fileSystem.deleteRecursive(dir);
    }

    @After
    public void after() throws IOException {
        fileSystem.deleteRecursive(dir);
    }

    @Test
    public void I_can_serve_a_file_from_the_filesystem() throws Exception {
        String contents = "Stuff in file";
        writeContentsTo("file.txt", dir, contents);
        HttpRequestProcessor processor = new HttpFileRequestProcessor();
        HttpTransactionProcessor httpProcessor = new SimpleHttpTransactionProcessor(processor, new SimpleContext(fileSystem));
        String contentsServed = getPage(httpProcessor,"/file.txt");

        assertEquals(contents,contentsServed);
    }

    private String getPage(HttpTransactionProcessor httpProcessor, String resource) throws Exception {
        HttpRequest request = HttpRequest.builder().method(GET).resource(resource).build();
        HttpResponse response = httpProcessor.process(request);
        return response.content;
    }

    private void writeContentsTo(String fileName, FileSystem.RelativePath dir, String contents) throws IOException {
        fileSystem.write(dir.resolve(fileName),contents.getBytes());
    }

    private FileSystem.RelativePath newDirectory() {
        return new SimpleRelativePath("tempDir");
    }
}
