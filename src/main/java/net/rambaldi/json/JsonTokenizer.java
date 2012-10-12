package net.rambaldi.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * For tokenizing Json StringS.
 */
public final class JsonTokenizer
    implements Iterable<String>
{
    private final String[] parts;

    public JsonTokenizer(String json) {
        this.parts = split(requireNonNull(json));
    }

    private String[] split(String json) {
        List<String> parts = new ArrayList<>();
        for (int i=0; i<json.length(); i++) {
            String s = json.substring(i, i + 1);
            if (!isWhitespace(s)) {
                if (isSingleLetterToken(s)) {
                    parts.add(s);
                }
                if (isDoubleQuote(s)) {
                    int end = endOfQuotedString(json, i+1);
                    parts.add(json.substring(i+1,end));
                    i = end;
                }
            }
        }
        return parts.toArray(new String[0]);
    }

    private int endOfQuotedString(String json, int start) {
        int at = json.indexOf("\"",start);
        if (at<0) {
            throw new IllegalArgumentException("No ending quote found starting at " + start + " in " + json);
        }
        return at;
    }

    private boolean isDoubleQuote(String string) {
        return string.equals("\"");
    }

    private boolean isSingleLetterToken(String string) {
        return string.equals("{") || string.equals("}") || string.equals(":");
    }

    private boolean isWhitespace(String string) {
        return string.equals(" ");
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {

            int i = 0;

            @Override
            public boolean hasNext() {
                return i < parts.length;
            }

            @Override
            public String next() {
                return parts[i++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
