package net.rambaldi.http;

import net.rambaldi.process.Transaction;
import net.rambaldi.process.TransactionSink;

import java.io.OutputStream;

public class HttpResponseWriter
    implements TransactionSink
{
    public HttpResponseWriter(OutputStream outputStream) {

    }

    @Override
    public void put(Transaction transaction) {
    }
}
