package net.rambaldi.http;

import net.rambaldi.process.Request;
import net.rambaldi.process.Response;
import net.rambaldi.process.TransactionProcessor;

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
