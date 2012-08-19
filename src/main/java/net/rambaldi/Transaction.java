package net.rambaldi;

/**
 * Base class for transactions.  Transactions should be immutable.
 * @author Curt
 */
public class Transaction 
    implements java.io.Serializable
{
    public final Timestamp timestamp;
    public final byte[] bytes;

    public Transaction(byte[] bytes,Timestamp timestamp) {
        this.bytes = bytes;
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
        if (!(o.getClass().equals(getClass()))) {
            return false;
        }
        Transaction that = (Transaction) o;
        if (!timestamp.equals(that.timestamp)) {
            return false;
        }
        if (bytes.length!=that.bytes.length) {
            return false;
        }
        for (int i=0; i<bytes.length; i++) {
            if (bytes[i]!=that.bytes[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return timestamp.hashCode();
    }
    
}
