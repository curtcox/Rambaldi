package net.rambaldi.http.rack;

public final class Response {
    /**
     The Status
     This is an HTTP status. It must be greater than or equal to 100.
     */
    public final int status;

    public final Headers headers;

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

    /**
     The Headers
     The header must respond to each, and yield values of key and value. The header keys must be Strings.
     The header must not contain a Status key, contain keys with : or newlines in their name,
     contain keys names that end in - or _, but only contain keys that consist of letters, digits,
     _ or - and start with a letter. The values of the header must be Strings, consisting of lines
     (for multiple header values, e.g. multiple Set-Cookie values) separated by “n“.
     The lines must not contain characters below 037.

     The Content-Type
     There must be a Content-Type, except when the Status is 1xx, 204 or 304, in which case there must be none given.

     The Content-Length
     There must not be a Content-Length header when the Status is 1xx, 204 or 304.
     */
    public static final class Headers {

    }

    public Response(int status, Headers headers, Body body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

}
