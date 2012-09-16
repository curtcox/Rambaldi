package net.rambaldi.http;

import net.rambaldi.process.Request;
import net.rambaldi.process.Timestamp;

import java.net.URI;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
/**
 * A single, immutable HttpRequest.
 */
public final class HttpRequest
    extends Request
{
    public final Method method;
    public final String resource;
    public final String from;
    public final String userAgent;
    public final String host;
    public final Connection connection;
    public final Version version;

    public static enum Connection {
        close, keepAlive;
    }

    public static enum Version {
        _1_0("1.0"), _1_1("1.1");
        String value;
        Version(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return value;
        }
    }

    private HttpRequest(Builder builder) {
        super(builder.value,builder.timestamp);
        this.method = requireNonNull(builder.method);
        this.resource = builder.resource;
        this.from = builder.from;
        this.userAgent = builder.userAgent;
        this.host = builder.host;
        this.connection = builder.connection;
        this.version = builder.version;
    }

    public static class Builder {
        private String value = "";
        private Timestamp timestamp = new Timestamp(0);
        private Method method = Method.GET;
        private String resource = "/";
        private String from;
        private String userAgent;
        private String host;
        private Connection connection;
        private Version version = Version._1_1;

        public Builder         resource(String resource) { this.resource = requireNonNull(resource); return this; }
        public Builder             method(Method method) { this.method = requireNonNull(method); return this; }
        public Builder                 from(String from) { this.from = from; return this; }
        public Builder       userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public Builder                 host(String host) { this.host = host; return this; }
        public Builder connection(Connection connection) { this.connection = connection; return this; }
        public Builder          version(Version version) { this.version = requireNonNull(version); return this; }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public enum Method {
        GET, POST
    }

    public String     method() { return format("%s %s HTTP/%s",method,resource,version); }
    public String       from() { return       from==null? "" : "From: "       + from; }
    public String  userAgent() { return  userAgent==null? "" : "User-Agent: " + userAgent; }
    public String       host() { return       host==null? "" : "Host: "       + host; }
    public String connection() { return connection==null? "" : "Connection: " + connection; }

    @Override
    public String toString() {
        return join(method(), from(), host(), userAgent(), connection());
    }

    private String join(String... lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            if (!line.isEmpty()) {
                builder.append(line + "\r\n");
            }
        }
        return builder.toString();
    }

}
