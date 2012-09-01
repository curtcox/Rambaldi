package net.rambaldi;

/**
 * Tools for dealing with transaction processors.
 */
public final class TransactionProcessors {

    public static StreamServer newExternal(StateOnDisk state) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("java",null);
        builder.directory(state.path.toFile());
        ProcessFactory processFactory = new SimpleProcessFactory(builder);
        return new ProcessAsStreamServer(processFactory);
    }
}
