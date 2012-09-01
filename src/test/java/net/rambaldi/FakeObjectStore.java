package net.rambaldi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FakeObjectStore
    implements ObjectStore
{
    private Map<Class<?>, Object> values = new HashMap<Class<?>, Object>();

    @Override
    public <T extends Serializable> void write(Class<T> type, T t) throws SerializationException {
        values.put(type,t);
    }

    @Override
    public <T> T read(Class<T> type) throws DeserializationException {
        return type.cast(values.get(type));
    }
}
