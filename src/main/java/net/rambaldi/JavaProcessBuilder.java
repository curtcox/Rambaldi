package net.rambaldi;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import static java.util.Objects.*;

public final class JavaProcessBuilder {

    private final ProcessBuilder builder = new ProcessBuilder();
    private final Path path;

    public JavaProcessBuilder(Path path) {
        this.path = requireNonNull(path);
    }

    public ProcessBuilder getConfigured() {
        builder.command(getCommand());
        builder.directory(path.toFile());
        return builder;
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
