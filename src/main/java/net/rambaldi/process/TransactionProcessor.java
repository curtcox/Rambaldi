package net.rambaldi.process;

import java.io.IOException;
import java.io.Serializable;

/**
 * Something that processes transactions.
 * See also RequestProcessor and ResponseProcessor.
 * @author Curt
 */
public interface TransactionProcessor
{
    Response process(Request request) throws Exception;
}
