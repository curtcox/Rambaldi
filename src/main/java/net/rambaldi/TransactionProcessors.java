package net.rambaldi;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.CheckedInputStream;

/**
 * Tools for dealing with transaction processors.
 */
public final class TransactionProcessors {

    public static StreamServer newExternal(InputStream in, OutputStream out, OutputStream err, StateOnDisk state) {
        Objects.requireNonNull(in);
        return new ProcessAsStreamServer(null);
    }
}
