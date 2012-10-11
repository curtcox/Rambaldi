package net.rambaldi.json;

import com.asynchrony.ionicmobile.Poll;

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
        return newInstance();
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
