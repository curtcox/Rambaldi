package net.rambaldi.process;

import java.nio.file.Path;

/**
 * Tools for dealing with transaction processors.
 */
public final class TransactionProcessors {

    public static StreamServer newExternal(StateOnDisk state) {
        Path path = state.path;
        ProcessBuilder builder = new JavaProcessBuilder(path).getConfigured();
        ProcessFactory processFactory = new SimpleProcessFactory(builder);
        return new ProcessAsStreamServer(processFactory, System.err);
    }

}
