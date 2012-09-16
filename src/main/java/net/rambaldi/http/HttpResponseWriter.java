package net.rambaldi.http;

import net.rambaldi.process.SerializationException;
import net.rambaldi.process.Transaction;
import net.rambaldi.process.TransactionSink;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

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
        System.out.println("writing 1" + transaction);
        if (!(transaction instanceof HttpResponse)) {
            throw new IllegalArgumentException(transaction + " is not a HttpResponse");
        }
        System.out.println("writing 2" + transaction);
        HttpResponse response = (HttpResponse) transaction;
        System.out.println("writing 3" + transaction);

        try {
            System.out.println("writing 4" + transaction);

            out.write(response.toString().getBytes());
            System.out.println("writing 5" + transaction);

        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
