package net.rambaldi.process;

import net.rambaldi.file.FileSystem;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import static java.util.Objects.*;

/**
 * For building a Java process.
 * In other words, for building a process that is running a JVM instance.
 */
public final class JavaProcessBuilder {

    private final ProcessBuilder builder = new ProcessBuilder();
    private final FileSystem.RelativePath path;

    public JavaProcessBuilder(FileSystem.RelativePath path) {
        this.path = requireNonNull(path);
    }

    public ProcessBuilder getConfigured() {
        builder.command(getCommand());
        builder.directory(file(path));
        return builder;
    }

    private File file(FileSystem.RelativePath path) {
        return null;
    }

    String[] getCommand() {
        return new String[]{"java", "-cp", getClasspath().toString(),Main.class.getCanonicalName()};
    }

    File getClasspath() {
        URL location = TransactionProcessors.class.getProtectionDomain().getCodeSource().getLocation();
        String external = location.toExternalForm();
        String FILE = "file:";
        if (external.startsWith(FILE)) {
            external = external.substring(FILE.length());
        }
        return new File(external);
    }

}
