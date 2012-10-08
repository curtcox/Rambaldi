package net.rambaldi.http;

import net.rambaldi.process.DeserializationException;
import net.rambaldi.process.TransactionSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static net.rambaldi.http.HttpRequest.*;

/**
 * For reading HttpResponseS from an InputStream.
 */
public final class HttpResponseReader
    implements TransactionSource
{

    public HttpResponseReader(InputStream inputStream) {
    }

    @Override
    public HttpResponse take() {
        return null;
    }

}
