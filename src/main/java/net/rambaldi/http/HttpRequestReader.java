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

public class HttpRequestReader
    implements TransactionSource
{
    private final InputStream inputStream;

    public HttpRequestReader(InputStream inputStream) {
        this.inputStream = requireNonNull(inputStream);
    }

    @Override
    public HttpRequest take() {
        try {
            Map<String,String> lines = readLines();
            if (lines.isEmpty()) {
                throw new DeserializationException();
            }
            return new HttpRequest("",new Timestamp(0), HttpRequest.Method.GET);
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }

    private Map<String,String> readLines() throws IOException {
        Map<String,String> lines = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        for (String line = reader.readLine(); line!=null; line=reader.readLine()) {
            lines.put(line,line);
        }
        return lines;
    }
}
