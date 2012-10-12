package net.rambaldi.json;

import com.asynchrony.ionicmobile.Poll;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * For converting between Json and objects.
 */
public final class Json <T> {

    private final Class<T> clazz;

    public Json(Class<T> clazz) {
        this.clazz = requireNonNull(clazz);
        try {
            clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    public T parse(String content) {
        T t = newInstance();
        setProperties(t, new JsonTokenizer(content).iterator());
        return t;
    }

    private void setProperties(T t, Iterator<String> tokens) {
        for (String token = tokens.next(); tokens.hasNext(); token = tokens.next()) {
            if (isKey(token)) {
                Object value = getValue(tokens);
                setProperty(t,token,value);
            }
        }
    }

    private void setProperty(T t,String fieldName, Object value) {
        try {
            Field field = t.getClass().getField(fieldName);
            field.set(t,value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    private Object getValue(Iterator<String> tokens) {
        if (!tokens.next().equals(":")) {
            throw new IllegalArgumentException("Assignment missing colon (:)");
        }

        return tokens.next();
    }

    private boolean isKey(String token) {
        return !(token.equals("{") || token.equals("}") || token.equals(":"));
    }

    private T newInstance() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String format(String... parts) {
        return null;
    }
}
