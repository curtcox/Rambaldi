package net.rambaldi.http;

import net.rambaldi.process.Transaction;
import net.rambaldi.process.TransactionSource;

import java.io.InputStream;

public class HttpRequestReader
    implements TransactionSource
{
    public HttpRequestReader(InputStream inputStream) {
    }

    @Override
    public Transaction take() {
        return null;
    }
}
