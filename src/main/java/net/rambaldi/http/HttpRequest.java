package net.rambaldi.http;

import net.rambaldi.process.Request;
import net.rambaldi.process.Timestamp;

import static java.util.Objects.requireNonNull;

public final class HttpRequest
    extends Request
{
    public final Method method;
    public final String resource = "/";

    public HttpRequest(String value, Timestamp timestamp, Method method) {
        super(value,timestamp);
        this.method = requireNonNull(method);
    }

    public enum Method {
        GET, POST
    }
}
