package net.rambaldi.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static net.rambaldi.file.FileSystem.RelativePath;

/**
 * A simple implementation of RelativePath.
 */
public final class SimpleRelativePath
    implements RelativePath
{
    private final List<String> elements;

    public SimpleRelativePath(String... elements) {
         this.elements = listFrom(elements);
    }

    private List<String> listFrom(String[] elements) {
        for (String string : elements) {
            if (requireNonNull(string).isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        return Arrays.asList(elements);
    }

    @Override
    public SimpleRelativePath resolve(String name) {
        List<String> list = new ArrayList<>();
        list.addAll(elements);
        list.add(name);
        return new SimpleRelativePath(list.toArray(new String[0]));
    }

    @Override
    public List<String> elements() {
        return elements;
    }

    @Override
    public boolean equals(Object object) {
        SimpleRelativePath that = (SimpleRelativePath) object;
        return elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}
