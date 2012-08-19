package net.rambaldi;

/**
 * Base class for transactions.  Transactions should be immutable.
 * @author Curt
 */
public abstract class Transaction {
    
    public final Timestamp timestamp;
    
    public Transaction(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
