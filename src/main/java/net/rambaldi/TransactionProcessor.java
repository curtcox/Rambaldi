package net.rambaldi;

import java.io.Serializable;

/**
 * Something that processes transactions.
 * See also RequestProcessor and ResponseProcessor.
 * @author Curt
 */
public interface TransactionProcessor
    extends Serializable
{
    void process();
}
