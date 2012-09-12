package net.rambaldi.http;

import net.rambaldi.process.SerializationException;
import net.rambaldi.process.Transaction;
import net.rambaldi.process.TransactionSink;

import java.io.IOException;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

/**
 * For writing HttpResponses to an OutputStream.
 */
public final class HttpResponseWriter
    implements TransactionSink
{
    private final OutputStream out;

    public HttpResponseWriter(OutputStream out) {
        this.out = requireNonNull(out);
    }

    @Override
    public void put(Transaction transaction) {
        HttpResponse response = (HttpResponse) transaction;
        try {
            out.write(response.toString().getBytes());
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
