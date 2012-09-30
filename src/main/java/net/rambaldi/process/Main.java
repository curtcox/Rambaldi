package net.rambaldi.process;

import net.rambaldi.Log.SimpleLog;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import static java.util.Objects.*;

/**
 * For creating an operating-system process to use as a TransactionProcessor.
 * The process reads requests from standard in and writes them to standard out.
 */
public final class Main {

    final IO io;
    final Callable system;
    final StateOnDisk state;

    Main(IO io, StateOnDisk state, InputStream in, OutputStream out, PrintStream err) {
        this.io = requireNonNull(io);
        this.state = requireNonNull(state);
        Context context = new SimpleContext();
        RequestProcessor requestProcessor = state.getRequestProcessor();
        ResponseProcessor responses = new SimpleResponseProcessor();
        system = new DebugCallable(
            new StreamTransactionProcessor(in,out,err,io,context,requestProcessor,responses)
        , new SimpleLog("StreamTransactionProcessor",err));
    }

    void run() throws Exception {
        for (;;) {
            system.call();
        }
    }

    public static void main(String[] args) throws Exception {
        IO io = new SimpleIO();
           io = new DebugIO(io,new SimpleLog("Main IO",System.err));
        StateOnDisk state = new StateOnDisk(Paths.get(""),io,new SimpleFileSystem());
        state.load();
        Main main = new Main(io,state,System.in,System.out,System.err);
        main.run();
    }
}
