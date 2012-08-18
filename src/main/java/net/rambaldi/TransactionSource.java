package net.rambaldi;

/**
 *
 * @author Curt
 */
public interface TransactionSource {
    
    Transaction take();
}
