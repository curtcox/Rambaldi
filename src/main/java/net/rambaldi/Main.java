package net.rambaldi;

import java.nio.file.Paths;

public final class Main {

    final IO io = new SimpleIO();
    final StreamTransactionProcessor system;
    final StateOnDisk state = new StateOnDisk(Paths.get(""),io);

    Main() {
        Context context = null;
        RequestProcessor requestProcessor = state.getProcessor();
        system = new StreamTransactionProcessor(System.in,System.out,System.err,io,context,requestProcessor,null);
    }

    void run() {
        for (;;) {
            system.process();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
}
