package net.rambaldi;

import java.io.File;
import java.net.URL;

/**
 * Tools for dealing with transaction processors.
 */
public final class TransactionProcessors {

    public static StreamServer newExternal(StateOnDisk state) {
        ProcessBuilder builder = new ProcessBuilder();
        File classpath = getClasspath();
        builder.command("java","-cp",classpath.toString(),Main.class.getCanonicalName());
        builder.directory(state.path.toFile());
        ProcessFactory processFactory = new SimpleProcessFactory(builder);
        return new ProcessAsStreamServer(processFactory);
    }

    private static File getClasspath() {
        URL location = TransactionProcessors.class.getProtectionDomain().getCodeSource().getLocation();
        String external = location.toExternalForm();
        return new File(external);
    }
}
