package net.rambaldi.process;

public class FakeTransactionProcessor
    implements TransactionProcessor
{
    public Response response;

    @Override
    public Response process(Request request) {
        return response;
    }
}
