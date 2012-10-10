package net.rambaldi.json;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Extend this to provide slow, but reasonable implementations of the equals, hashCode, and toString methods.
 */
public class MutableObject {

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass()!=object.getClass()) {
            return false;
        }
        MutableObject that = (MutableObject) object;
        for (Field field :getClass().getFields()) {
            if (!Objects.equals(get(field),that.get(field))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getClass().getSimpleName() + "{");
        for (Field field :getClass().getFields()) {
              out.append(field.getName() + "=" + get(field));
        }
        out.append("}");
        return out.toString();
    }

    private Object get(Field field) {
        try {
            return field.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
