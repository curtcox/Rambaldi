package tests.acceptance;

import java.io.Serializable;
import net.rambaldi.IO;

/**
 *
 * @author Curt
 */
public final class Copier {

    public static <T extends Serializable> T copy(T serializable) {
        Serializable copy = IO.deserialize(IO.serialize(serializable));
        return (T) copy;
    }

}
