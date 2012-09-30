package net.rambaldi.http;

/**
 * Something that processes HTTP Transactions.
 * Conceptually, a HttpTransaction is a type of TransactionProcessor.  There is no reasonable way to show that in
 * the type system, however because HttpTransactionProcessors only accept HttpRequests.
 *
 * See also TransactionProcessor.
 */
public interface HttpTransactionProcessor
{
    /**
     * Process the given request and produce the appropriate response.
     */
    HttpResponse process(HttpRequest request) throws Exception;
}
