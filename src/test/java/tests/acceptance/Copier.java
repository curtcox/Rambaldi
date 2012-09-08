package tests.acceptance;

import java.io.Serializable;
import net.rambaldi.process.IO;
import net.rambaldi.process.SimpleIO;

/**
 *
 * @author Curt
 */
public final class Copier {

    public static <T extends Serializable> T copy(T serializable) {
        IO io = new SimpleIO();
        Serializable copy = io.deserialize(io.serialize(serializable));
        return (T) copy;
    }

}
