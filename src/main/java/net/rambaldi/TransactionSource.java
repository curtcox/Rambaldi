package net.rambaldi;

/**
 * Something that Transactions can be taken from.
 * @author Curt
 */
public interface TransactionSource {
    
    Transaction take();
}
