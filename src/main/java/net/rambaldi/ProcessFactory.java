package net.rambaldi;

import java.io.IOException;

/**
 * Something that creates a new started processes.
 */
interface ProcessFactory {

    Process newInstance() throws ProcessCreationException;
}
