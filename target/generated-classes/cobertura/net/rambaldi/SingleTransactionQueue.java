package net.rambaldi;

/**
 *
 * @author Curt
 */
public class SingleTransactionQueue
    implements TransactionSource, TransactionSink
{
    private Transaction transaction;
    private boolean empty = true;
    
    @Override
    public Transaction take() {
        empty = true;
        return transaction;
    }

    @Override
    public void put(Transaction transaction) {
        empty = false;
        this.transaction = transaction;
    }
 
    public boolean isEmpty() {
        return empty;
    }
}
