package net.rambaldi.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface HttpConnection {

    interface Factory extends AutoCloseable {
        HttpConnection accept() throws IOException;
    }

    InputStream getInputStream();

    OutputStream getOutputStream();
}
