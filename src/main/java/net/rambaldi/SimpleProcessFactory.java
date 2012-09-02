package net.rambaldi;

import static java.util.Objects.*;

final class SimpleProcessFactory
    implements ProcessFactory
{

    private final ProcessBuilder builder;

    SimpleProcessFactory(ProcessBuilder builder) {
        this.builder = requireNonNull(builder);
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
