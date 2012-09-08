package net.rambaldi.process;

/**
 * Base class for transactions.  Transactions should be immutable.
 * @author Curt
 */
public interface Transaction 
    extends java.io.Serializable, Immutable
{
    Timestamp getTimestamp();
}
