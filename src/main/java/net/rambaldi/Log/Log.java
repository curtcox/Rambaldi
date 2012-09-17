package net.rambaldi.Log;

public interface Log {
    void info(Object message);
    void throwable(Throwable t);
}
