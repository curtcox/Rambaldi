package net.rambaldi;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import static java.util.Objects.*;

public final class Main {

    final IO io;
    final StreamTransactionProcessor system;
    final StateOnDisk state;

    Main(IO io, StateOnDisk state, InputStream in, OutputStream out, OutputStream err) {
        this.io = requireNonNull(io);
        this.state = requireNonNull(state);
        Context context = new SimpleContext();
        RequestProcessor requestProcessor = state.getProcessor();
        ResponseProcessor responses = new SimpleResponseProcessor();
        system = new StreamTransactionProcessor(in,out,err,io,context,requestProcessor,responses);
    }

    void run() {
        for (;;) {
            system.process();
        }
    }

    public static void main(String[] args) {
        IO io = new SimpleIO();
        //   io = new DebugIO(io,System.err);
        StateOnDisk state = new StateOnDisk(Paths.get(""),io,new SimpleFileSystem());
        state.load();
        Main main = new Main(io,state,System.in,System.out,System.err);
        main.run();
    }
}
