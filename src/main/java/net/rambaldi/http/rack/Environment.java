package net.rambaldi.http.rack;

import net.rambaldi.http.HttpRequest;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static net.rambaldi.http.HttpRequest.ContentType;
import static net.rambaldi.http.HttpRequest.Method;

/**
 The environment must be an instance of Map that includes CGI-like headers.
 The application is free to modify the environment.
 The environment is required to include these variables (adopted from PEP333), except when they’d be empty, but see below.
 */
public final class Environment
{
    public HttpRequest request;

    /**
     The HTTP request method, such as “GET” or “POST”.
     This is always required.
     The REQUEST_METHOD must be a valid token.
     */
    public final Method method;

    public final int contentLength;

    public final ContentType contentType;

    /**
     The initial portion of the request URL’s “path” that corresponds to the application object,
     so that the application knows its virtual “location”.
     This may be an empty string, if the application corresponds to the “root” of the server.
     The SCRIPT_NAME, if non-empty, must start with /
     One of SCRIPT_NAME or PATH_INFO must be set. PATH_INFO should be / if SCRIPT_NAME is empty.
     SCRIPT_NAME never should be /, but instead be empty.
     */
    public final String scriptName;

    /**
     The remainder of the request URL’s “path”,
     designating the virtual “location” of the request’s target within the application.
     This may be an empty string, if the request URL targets the application root and does not have a trailing slash.
     This value may be percent-encoded when originating from a URL.
     The PATH_INFO, if non-empty, must start with /
     */
    public final String path;

    /**
     The portion of the request URL that follows the ?, if any. May be empty, but is always required!
     */
    public final String queryString;

    /**
       When combined with serverPort, scriptName, and path, this variable can be used to complete the URL.
       Note, however, that HTTP_HOST, if present, should be used in preference to SERVER_NAME for reconstructing the request URL.
       SERVER_NAME and SERVER_PORT can never be empty strings, and so are always required.
     */
    public final String serverName;

    /**
     When combined with serverName, scriptName, and path, this variable can be used to complete the URL.
     Note, however, that HTTP_HOST, if present, should be used in preference to SERVER_NAME
     for reconstructing the request URL.
     SERVER_NAME and SERVER_PORT can never be empty strings, and so are always required.
     */
    public final int serverPort;

    public final UrlScheme urlScheme;

    /**
     true if the application object may be simultaneously invoked by another thread in the same process, false otherwise.
     */
    public final boolean multithread = false;

    /**
     true if an equivalent application object may be simultaneously invoked by another process, false otherwise.
     */
    public final boolean multiprocess = false;

    /**
     true if the server expects (but does not guarantee!) that the application will only be invoked this one time during the life of its containing process.
     Normally, this will only be true for a server based on CGI (or something similar).
     Additional environment specifications have approved to standardized middleware APIs.
     None of these are required to be implemented by the server.
     */
    public final boolean run_once = false;

    public final Version version;

    /**
     There must be a valid input stream in rack.input.
     */
    public final InputStream inputStream;

    /**
     There must be a valid error stream in rack.errors.
     */
    public final ErrorStream errorStream;

    public final Session session;
    public final Logger logger;

    public Environment(Builder builder) {
        this.request = builder.request;
        method = request.method;
        contentLength = request.contentLength;
        contentType = request.contentType;
        errorStream = builder.errorStream;
        session = builder.session;
        inputStream = builder.inputStream;
        version = builder.version;
        logger = builder.logger;
        scriptName = null;
        path = null;
        queryString = null;
        serverName = null;
        serverPort = 0;
        urlScheme = null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private HttpRequest     request = HttpRequest.builder().build();
        private InputStream inputStream = new InputStream();
        private ErrorStream errorStream = new ErrorStream();
        private Session         session = new Session();
        private Logger           logger = new Logger();
        private Version         version = new Version();
        public Environment build() {
            return new Environment(this);
        }

        public Builder         request(HttpRequest request) { this.request     = requireNonNull(request);     return this; }
        public Builder inputStream(InputStream inputStream) { this.inputStream = requireNonNull(inputStream); return this; }
        public Builder errorStream(ErrorStream errorStream) { this.errorStream = requireNonNull(errorStream); return this; }
        public Builder             session(Session session) { this.session     = requireNonNull(session);     return this; }
        public Builder                logger(Logger logger) { this.logger      = requireNonNull(logger);      return this; }
        public Builder             version(Version version) { this.version     = requireNonNull(version);     return this; }
    }
}
