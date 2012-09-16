package net.rambaldi.http;

import net.rambaldi.process.*;

import javax.xml.stream.util.StreamReaderDelegate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * For reading HttpRequestS from an InputStream.
 */
public final class HttpRequestReader
    implements TransactionSource
{
    private BufferedReader reader;

    public HttpRequestReader(InputStream inputStream) {
        reader = new BufferedReader(new InputStreamReader(requireNonNull(inputStream)));
    }

    @Override
    public HttpRequest take() {
        try {
            Map<String,String> lines = readLines();
            if (lines.isEmpty()) {
                throw new DeserializationException();
            }
            return HttpRequest.builder().build();
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }

    private Map<String,String> readLines() throws IOException {
        Map<String,String> lines = new HashMap<>();
        for (String line = reader.readLine(); line!=null; line=reader.readLine()) {
            if (line.trim().isEmpty()) {
                return lines;
            }
            lines.put(line,line);
        }
        return lines;
    }
}
