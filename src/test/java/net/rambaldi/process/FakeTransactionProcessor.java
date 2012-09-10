package net.rambaldi.process;

public class FakeTransactionProcessor
    implements TransactionProcessor
{
    @Override
    public void process() {
    }

    @Override
    public Response process(Request request) {
        return null;
    }
}
