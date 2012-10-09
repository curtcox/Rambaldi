package net.rambaldi.process;

import net.rambaldi.time.Immutable;
import net.rambaldi.time.Timestamp;

/**
 * Base class for transactions.  Transactions should be immutable.
 * @author Curt
 */
public interface Transaction 
    extends java.io.Serializable, Immutable
{
    Timestamp getTimestamp();
}
