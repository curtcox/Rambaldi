package net.rambaldi.http.rack;

import java.util.Map;

/**
 * A Rack application is an object that responds to call.
 * It takes exactly one argument, the environment and returns a Response with three parts:
 * The status, the headers, and the body.
 * The Rack API in Ruby plays roughly the same role as the Servlet API in Java.
 * <p>
 * This interface exists to support:
 * <ul>
 *     <li>Applications written to the Ruby Rack spec.</li>
 *     <li>Java programs that would like to use a similar interface.</li>
 *     <li>Java web servers that want to support Rack more directly than via ServletS.</li>
 * </ul>
 * It is adapted from the Ruby Rack spec.
 * See http://rack.rubyforge.org/doc/SPEC.html
 */
public interface Rack {

    Response call(Environment env);

}
