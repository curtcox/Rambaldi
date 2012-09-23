package net.rambaldi.http;

import net.rambaldi.Log.SimpleLog;
import net.rambaldi.process.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static net.rambaldi.http.HttpRequest.Accept;
import static net.rambaldi.http.HttpRequest.Builder;
import static net.rambaldi.http.HttpRequest.Connection;

/**
 * For reading HttpRequestS from an InputStream.
 */
public final class HttpRequestReader
    implements TransactionSource
{
    private BufferedReader reader;
    private static final String connection = "connection";
    private static final String get = "get";
    private static final String host = "host";
    private static final String user_agent = "user-agent";
    private static final String accept = "accept";

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
            Builder builder = HttpRequest.builder();
            setHostFrom(builder, lines);
            setUserAgentFrom(builder, lines);
            setResourceFrom(builder, lines);
            setConnectionFrom(builder, lines);
            setAcceptFrom(builder, lines);
            return builder.build();
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }

    private void setResourceFrom(Builder builder, Map<String,String> lines) {
        if (lines.containsKey(get)) {
            builder.resource(lines.get(get));
        }
    }

    private void setHostFrom(Builder builder, Map<String,String> lines) {
        if (lines.containsKey(host)) {
            builder.host(lines.get(host));
        }
    }

    private void setUserAgentFrom(Builder builder, Map<String,String> lines) {
        if (lines.containsKey(user_agent)) {
            builder.userAgent(lines.get(user_agent));
        }
    }

    private void setAcceptFrom(Builder builder, Map<String,String> lines) {
        if (lines.containsKey(accept)) {
            builder.accept(new Accept(lines.get(accept)));
        }
    }

    private void setConnectionFrom(Builder builder, Map<String,String> lines) {
        if (lines.containsKey(connection)) {
            String stripped = lines.get(connection).replace("-","_");
            builder.connection(Connection.valueOf(stripped));
        }
    }

    private Map<String,String> readLines() throws IOException {
        Map<String,String> lines = new HashMap<>();
        for (String line = reader.readLine(); line!=null; line=reader.readLine()) {
            if (line.trim().isEmpty() && !lines.isEmpty()) {
                return lines;
            }
            String[] parts = splitAfterFirstColon(line);
            if (parts.length==2) {
                String key = parts[0].toLowerCase();
                String value = parts[1];
                lines.put(key,value);
            }
            if (line.startsWith("GET")) {
                lines.put(get,line.split(" ")[1]);
            }
        }
        return lines;
    }

    private String[] splitAfterFirstColon(String line) {
        if (!line.contains(":")) {
            return new String[0];
        }
        int colon = line.indexOf(":");
        String before = line.substring(0,colon).trim();
        String after = line.substring(colon+1).trim();
        return new String[] {before,after};
    }
}
