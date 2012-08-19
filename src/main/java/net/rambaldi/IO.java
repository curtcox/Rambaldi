package net.rambaldi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Curt
 */
public final class IO {

    public static byte[] serialize(Serializable serializable) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
                out.writeObject(serializable);
            }
            return bytes.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public static Serializable deserialize(byte[] bytes) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (Serializable) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new DeserializationException(e);
        }
    }

}
