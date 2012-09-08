package net.rambaldi.process;

/**
 * Something that Transactions can be put to.
 * @author Curt
 */
public interface TransactionSink {

    void put(Transaction transaction);
}
