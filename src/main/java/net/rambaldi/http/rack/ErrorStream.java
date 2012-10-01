package net.rambaldi.http.rack;

/*
The Error Stream
The error stream must respond to puts, write and flush.

puts must be called with a single argument that responds to to_s.
write must be called with a single argument that is a String.
flush must be called without arguments and must be called in order to make the error appear for sure.
close must never be called on the error stream.
*/
public final class ErrorStream {
}
