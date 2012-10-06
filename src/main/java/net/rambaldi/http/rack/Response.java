package net.rambaldi.http.rack;

public final class Response {
    /**
     The Status
     This is an HTTP status. It must be greater than or equal to 100.
     */
    public final int status;

    /**
     The Content-Type
     There must be a Content-Type, except when the Status is 1xx, 204 or 304, in which case there must be none given.

     The Content-Length
     There must not be a Content-Length header when the Status is 1xx, 204 or 304.
     */
    public final ResponseHeaders headers;

    public final Body body;

    /**

     The Body
     The Body must respond to each and must only yield String values.
     The Body itself should not be an instance of String, as this will break in Ruby 1.9.
     If the Body responds to close, it will be called after iteration.
     If the Body responds to to_path, it must return a String identifying the location of a file whose contents
     are identical to that produced by calling each; this may be used by the server as an alternative,
     possibly more efficient way to transport the response.
     The Body commonly is an Array of Strings, the application instance itself, or a File-like object.
     */
    public static final class Body {

        public Body(String s) {
        }
    }

    public Response(int status, ResponseHeaders headers, Body body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

}
