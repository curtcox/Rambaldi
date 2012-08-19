package net.rambaldi;

/**
 * An immutable, typesafe timestamp.
 * @author Curt
 */
public final class Timestamp
    implements java.io.Serializable
{
    
    public final long millis;
    
    public Timestamp(long millis) {
        this.millis = millis;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this==o) {
            return true;
        }
        if (!(o instanceof Timestamp)) {
            return false;
        }
        Timestamp that = (Timestamp) o;
        return this.millis == that.millis;
    }

    @Override
    public int hashCode() {
        return (int) millis;
    }

}
