package net.rambaldi.process;

/**
 * Something that Transactions can be taken from.
 * @author Curt
 */
public interface TransactionSource {

    /**
     * Return the next transaction.
     * If no transactions are available, the behaviour is implementation specific.
     * Namely, this method might block or throw an exception.
     * @return the transaction if it is available
     */
    Transaction take();
}
