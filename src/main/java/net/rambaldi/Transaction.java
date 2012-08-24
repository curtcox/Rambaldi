package net.rambaldi;

/**
 * Base class for transactions.  Transactions should be immutable.
 * @author Curt
 */
public interface Transaction 
    extends java.io.Serializable
{
    Timestamp getTimestamp();
}
