package net.rambaldi;

/**
 *
 * @author Curt
 */
public interface TransactionSink {

    void put(Transaction transaction);
}
