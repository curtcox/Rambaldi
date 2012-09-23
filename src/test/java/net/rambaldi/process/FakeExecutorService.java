package net.rambaldi.process;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class FakeExecutorService implements ExecutorService {

    public boolean isExecuting;
    public ExecutionException threw;
    public boolean interrupted;
    public boolean isShutdown;

    @Override
    public void execute(Runnable command) {
        isExecuting = true;
        FutureTask task = (FutureTask) command;
        try {
            task.run();
            task.get();
        } catch (InterruptedException e) {
            interrupted = true;
        } catch (ExecutionException e) {
            threw = e;
        } finally {
            isExecuting = false;
        }
    }

    @Override
    public void shutdown() {
        isShutdown = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        FutureTask task = new FutureTask(callable);
        task.run();
        return task;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return null;
    }

    @Override
    public Future<?> submit(Runnable task) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
