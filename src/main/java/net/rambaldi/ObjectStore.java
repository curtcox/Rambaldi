package net.rambaldi;

import java.io.Serializable;

/**
 * Something that can be used to read and write objects.
 */
public interface ObjectStore {

    <T extends Serializable> void write(Class<T> clazz, T t) throws SerializationException;

    <T> T read(Class<T> t) throws DeserializationException;
}
