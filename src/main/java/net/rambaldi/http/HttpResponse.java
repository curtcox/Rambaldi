package net.rambaldi.http;

import net.rambaldi.process.Request;
import net.rambaldi.process.Response;
import net.rambaldi.process.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import static java.util.Objects.requireNonNull;

/**
 * An immutable HttpResponse.
 */
public final class HttpResponse
    extends Response
{
    public final Status status;
    public final Timestamp date;
    public final ContentType contentType;
    public final String content;
    public final HttpRequest request;

    public static enum Status {
        OK(200);
        int code;
        Status(int code) {
            this.code = code;
        }
        @Override
        public String toString() {
            return code + " " + name();
        }
    }

    public static enum ContentType {
        TextHtml("text/html"), TextPlain("text/plain");
        String value;
        ContentType(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return value;
        }
    }

    private HttpResponse(Builder builder) {
        super(builder.value, builder.request);
        this.status      = builder.status;
        this.date        = builder.date;
        this.contentType = builder.contentType;
        this.content     = builder.content;
        this.request     = builder.request;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private      String value = "";
        private HttpRequest request;
        private      Status status = Status.OK;
        private   Timestamp date = new Timestamp(0);
        private ContentType contentType = ContentType.TextHtml;
        private      String content = "";

        public HttpResponse build() {
            requireNonNull(request);
            return new HttpResponse(this);
        }

        public Builder         request(HttpRequest request) { this.request = requireNonNull(request); return this; }
        public Builder                status(Status status) { this.status = requireNonNull(status); return this; }
        public Builder            date(Timestamp timestamp) { this.date = requireNonNull(timestamp); return this; }
        public Builder contentType(ContentType contentType) { this.contentType = requireNonNull(contentType); return this; }
        public Builder              content(String content) { this.content = requireNonNull(content); return this; }
    }

    public String        status() { return "HTTP/1.0 " + status; }
    public String          date() { return "Date: " + format(date); }
    public String   contentType() { return "Content-Type: " + contentType.toString(); }
    public String contentLength() { return "Content-Length: " + content.length(); }
    public String       content() { return content; }

    private static String format(Timestamp timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        format.setCalendar(calendar);
        return format.format(new Date(timestamp.millis));
    }

    @Override
    public String toString() {
        return join(status(), date(), contentType(), contentLength(), "", content());
    }

    private String join(String... lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line + "\r\n");
        }
        return builder.toString();
    }

}
