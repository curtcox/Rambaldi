package net.rambaldi.http;

public class FakeHttpTransactionProcessor
    implements HttpTransactionProcessor
{
    public HttpResponse response;

    @Override
    public HttpResponse process(HttpRequest request) {
        return response;
    }
}
