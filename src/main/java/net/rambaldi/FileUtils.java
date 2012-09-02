package net.rambaldi;

import java.io.File;
import java.io.FileNotFoundException;

public final class FileUtils {
    /**
     * By default File#delete fails for non-empty directories, it works like "rm".
     * We need something a little more brutual - this does the equivalent of "rm -r"
     * @param path Root File Path
     */
    public static void deleteRecursive(File path) {
        if (!path.exists()) {
            return;
        }
        if (path.isDirectory()){
            for (File f : path.listFiles()){
                FileUtils.deleteRecursive(f);
            }
        }
    }
}
