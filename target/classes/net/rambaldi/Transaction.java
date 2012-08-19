package net.rambaldi;

/**
 * Base class for transactions.  Transactions should be immutable.
 * @author Curt
 */
public class Transaction 
    implements java.io.Serializable
{
    public final Timestamp timestamp;
    
    public Transaction(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this==o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        Transaction that = (Transaction) o;
        return this.timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return timestamp.hashCode();
    }
    
}
