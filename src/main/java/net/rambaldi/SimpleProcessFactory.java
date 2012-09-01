package net.rambaldi;

import java.io.IOException;
import java.util.Objects;

final class SimpleProcessFactory
    implements ProcessFactory
{

    private final ProcessBuilder builder;

    SimpleProcessFactory(ProcessBuilder builder) {
        this.builder = Objects.requireNonNull(builder);
    }

    @Override
    public Process newInstance() throws ProcessCreationException {
        try {
            return builder.start();
        } catch (Exception e) {
            throw new ProcessCreationException(e);
        }
    }
}
