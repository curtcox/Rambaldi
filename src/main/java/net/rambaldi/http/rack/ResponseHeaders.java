package net.rambaldi.http.rack;

import net.rambaldi.time.Immutable;

import java.util.*;

/**
 The Headers
 <ul>
      <li>
          must respond to each, and yield values of key and value. The header keys must be Strings.
      </li>
      <li>
         The header must not contain a Status key, contain keys with : or newlines in their name,
         contain keys names that end in - or _, but only contain keys that consist of letters, digits,
         _ or - and start with a letter.
      </li>
      <li>
         The values of the header must be Strings, consisting of lines
         (for multiple header values, e.g. multiple Set-Cookie values) separated by “n“.
         The lines must not contain characters below 037.
      </li>
      <li>
         The Content-Type
         There must be a Content-Type, except when the Status is 1xx, 204 or 304, in which case there must be none given.
      </li>
      <li>
         The Content-Length
         There must not be a Content-Length header when the Status is 1xx, 204 or 304.
      </li>
 </ul>
 */
public final class ResponseHeaders
    implements Immutable, Map<String,String>
{

    private final Map<String,String> headers = new HashMap<>();

    public ResponseHeaders() {
        this(new HashMap());
    }

    public ResponseHeaders(Map<String,String> map) {
        for (Object key : map.keySet()) {
            requireValidKey(requireString(key));
            requireString(map.get(key));
        }
        headers.putAll(map);
    }

    private void requireValidKey(String key) {
        if (!isValidKey(key)) {
            throw new IllegalArgumentException(
               key +
               " is not valid.  The header must not contain a Status key, contain keys with : or newlines in their name," +
               " contain keys names that end in - or _, but only contain keys that consist of letters, digits," +
               " _ or - and start with a letter."
            );
        }
    }

    private boolean isValidKey(String key) {
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (key.length() == 0 || !in(key.charAt(0), letters)) {
            return false;
        }
        if (in(key.charAt(key.length()-1),"-_")) {
            return false;
        }
        for (char c : key.toCharArray()) {
            if (!in(c, letters + "0123456789-_")) {
                return false;
            }
        }
        if (key.toLowerCase().equals("status")) {
            return false;
        }
        return true;
    }

    private static boolean in(char c , String chars) {
        Set<Character> set = new HashSet<>();
        for (char x : chars.toCharArray()) {
            set.add(x);
        }
        return set.contains(c);
    }

    private static String requireString(Object object) {
        if (object instanceof String) {
            return (String) object;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int size() {
        return headers.size();
    }

    @Override
    public boolean isEmpty() {
        return headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(Object key) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Set<String> keySet() {
        return headers.keySet();
    }

    @Override
    public Collection<String> values() {
        return headers.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (String key : headers.keySet()) {
            String value = headers.get(key);
            out.append(String.format("\"%s\"=\"%s\"",key,value));
        }
        return out.toString();
    }

    // We are immutable
    @Override public String put(String key, String value) { throw new UnsupportedOperationException(); }
    @Override public String remove(Object key) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends String, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
}
