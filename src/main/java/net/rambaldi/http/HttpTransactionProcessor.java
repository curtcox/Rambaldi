package net.rambaldi.http;

/**
 * Something that processes an HTTP Transaction.
 * See also TransactionProcessor.
 */
public interface HttpTransactionProcessor
{
    /**
     * Process the given request and produce the appropriate response.
     */
    HttpResponse process(HttpRequest request) throws Exception;
}
