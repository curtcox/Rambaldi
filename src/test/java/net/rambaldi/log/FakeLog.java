package net.rambaldi.Log;

public class FakeLog
    implements Log
{

    public Object message;
    public Throwable exception;

    @Override
    public void info(Object message) {
        this.message = message;
    }

    @Override
    public void throwable(Throwable t) {
        exception = t;
    }


}
