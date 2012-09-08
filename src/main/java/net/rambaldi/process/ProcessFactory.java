package net.rambaldi.process;

/**
 * Something that creates a new started processes.
 */
interface ProcessFactory {

    Process newInstance() throws ProcessCreationException;
}
