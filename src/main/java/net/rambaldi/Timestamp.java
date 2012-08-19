package net.rambaldi;

/**
 * An immutable, typesafe timestamp.
 * @author Curt
 */
public final class Timestamp {
    
    public final long millis;
    
    public Timestamp(long millis) {
        this.millis = millis;
    }
}
