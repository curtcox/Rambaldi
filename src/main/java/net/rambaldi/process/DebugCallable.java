package net.rambaldi.process;

import net.rambaldi.Log.Log;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;

public final class DebugCallable
    implements Callable
{
    private final Callable callable;
    private final Log out;

    public DebugCallable(Callable callable, Log out) {
        this.callable = callable;
        this.out = out;
    }

    @Override
    public Object call() throws Exception {
        out.info("calling " + callable);
        Object result = callable.call();
        out.info(callable + " returned " + result);
        return result;
    }
}
