package net.rambaldi.Log;

import java.io.PrintStream;

public final class SimpleLog
    implements Log
{
    private final String prefix;
    private final PrintStream out;

    public SimpleLog(String prefix, PrintStream out) {
        this.prefix = prefix;
        this.out = out;
    }

    @Override
    public void info(Object message) {
        out.println(prefix + ":" + message);
    }

    @Override
    public void throwable(Throwable t) {
        t.printStackTrace(out);
    }
}
