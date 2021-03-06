package net.rambaldi.process;

import net.rambaldi.Log.Log;

import java.nio.file.Path;

/**
 * Tools for dealing with transaction processors.
 */
public final class TransactionProcessors {

    public static TransactionProcessor newInstance(StateOnDisk state) {
        throw new UnsupportedOperationException();
    }

    public static TransactionProcessor newExternal(StateOnDisk state, IO io, Log log) throws ProcessCreationException {
        ProcessBuilder builder = new JavaProcessBuilder(state.path).getConfigured();
        ProcessFactory processFactory = new SimpleProcessFactory(builder);
        StreamServer streams = new ProcessAsStreamServer(processFactory, System.err);
        streams.start();
        return new StreamServerAsTransactionProcessor(streams,io,log);
    }

}
