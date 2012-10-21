package net.rambaldi.http;

import net.rambaldi.time.Immutable;
import net.rambaldi.process.Request;
import net.rambaldi.time.Timestamp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
/**
 * A single, immutable HttpRequest.
 */
public final class HttpRequest
    extends Request
{
    public final Method method;
    public final Resource resource;
    public final String from;
    public final String userAgent;
    public final String host;
    public final Connection connection;
    public final Version version;
    public final Accept accept;
    public final String content;
    public final ContentType contentType;
    public final String queryString;

    public static class Resource implements Immutable, Serializable {

        public final String name;

        public Resource(String name) {
            this.name = requireNonNull(name);
            if (!name.startsWith("/")) {
                throw new IllegalArgumentException("Must start with /");
            }
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object object) {
            Resource that = (Resource) object;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    /**
     * In the future, this will be more than just a string wrapper
     * See https://tools.ietf.org/html/rfc2616#section-14.1
     */
    public static final class Accept implements Immutable, Serializable {
        public final String value;
        Accept(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return value;
        }
    }

    public static enum ContentType {
        UrlEncodedForm("application/x-www-form-urlencoded");
        String value;
        ContentType(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return value;
        }
    }

    public static enum Connection {
        close("close"), keep_alive("keep-alive");
        String value;
        Connection(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return value;
        }
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
        this.accept = builder.accept;
        this.content = builder.content;
        this.contentType = builder.contentType;
        queryString = queryString(builder.params);
    }

    private static String queryString(Map<String,String> params) {
        StringBuilder out = new StringBuilder();
        for (String key : params.keySet()) {
            out.append(key + "=" + params.get(key));
        }
        return out.toString();
    }

    public static class Builder {
        private String value = "";
        private Timestamp timestamp = new Timestamp(0);
        private Method method = Method.GET;
        private Resource resource = new Resource("/");
        private String from;
        private String userAgent;
        private String host;
        private Connection connection = Connection.keep_alive;
        private Version version = Version._1_1;
        private Accept accept;
        private ContentType contentType;
        private String content = "";
        private Map<String,String> params = new HashMap<>();

        public Builder                resource(String name) { this.resource(requireNonNull(new Resource(name))); return this; }
        public Builder          resource(Resource resource) { this.resource = requireNonNull(resource); return this; }
        public Builder                method(Method method) { this.method = requireNonNull(method); return this; }
        public Builder                    from(String from) { this.from = from; return this; }
        public Builder          userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public Builder                    host(String host) { this.host = host; return this; }
        public Builder    connection(Connection connection) { this.connection = requireNonNull(connection); return this; }
        public Builder             version(Version version) { this.version = requireNonNull(version); return this; }
        public Builder                accept(Accept accept) { this.accept = requireNonNull(accept); return this; }
        public Builder contentType(ContentType contentType) { this.contentType = contentType; return this; }
        public Builder              content(String content) { this.content = requireNonNull(content); return this; }
        public Builder             params(String... params) {
            for (int i=0; i<params.length; i= i+2) {
                this.params.put(params[i],params[i+1]);
            }
            return this;
        }

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

    public String        method() { return format("%s %s HTTP/%s",method,resource,version); }
    public String          from() { return        from==null? "" : "From: "           + from; }
    public String     userAgent() { return   userAgent==null? "" : "User-Agent: "     + userAgent; }
    public String        accept() { return      accept==null? "" : "Accept: "         + accept; }
    public String          host() { return        host==null? "" : "Host: "           + host; }
    public String    connection() { return  connection==null? "" : "Connection: "     + connection; }
    public String   contentType() { return contentType==null? "" : "Content-Type: "   + contentType; }
    public String contentLength() { return content.isEmpty()? "" : "Content-Length: " + content.length(); }
    public String       content() { return content;}

    @Override
    public String toString() {
        return join(
                method(), from(), userAgent(), host(), accept(), contentType(), contentLength(), connection(),
                separateIfNotEmpty(content())
        );
    }

    private String separateIfNotEmpty(String content) {
        return content.isEmpty() ? content() : "\r\n" + content();
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

    @Override
    public boolean equals(Object o) {
        HttpRequest that = (HttpRequest) o;
        return toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
