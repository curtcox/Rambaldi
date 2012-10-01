package net.rambaldi.http.rack;

/**
 rack.logger:	A common object interface for logging messages. The object must implement:
 info(message, &block)
 debug(message, &block)
 warn(message, &block)
 error(message, &block)
 fatal(message, &block)
 The server or the application can store their own data in the environment, too. The keys must contain at least one dot, and should be prefixed uniquely.
 The prefix rack. is reserved for use with the Rack core distribution and other accepted specifications and must not be used otherwise.
 The environment must not contain the keys HTTP_CONTENT_TYPE or HTTP_CONTENT_LENGTH (use the versions without HTTP_).
 The CGI keys (named without a period) must have String values. There are the following restrictions:
 */
public class Logger {
}
