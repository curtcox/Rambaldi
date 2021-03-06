package net.rambaldi.process;

import net.rambaldi.file.SimpleRelativePath;
import net.rambaldi.process.JavaProcessBuilder;
import net.rambaldi.process.Main;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JavaProcessBuilderTest {

    JavaProcessBuilder builder = new JavaProcessBuilder(new SimpleRelativePath());

    @Test(expected = NullPointerException.class)
    public void constructor_requires_path() {
        new JavaProcessBuilder(null);
    }

    @Test
    public void getConfigured_returns_ProcessBuilder() {
        assertNotNull(builder.getConfigured());
    }

    @Test
    public void getClasspath_returns_existing_directory() {
        File file = builder.getClasspath();
        assertTrue(file.toString() + " does not exist",file.exists());
    }

    @Test
    public void getClasspath_returns_directory_with_classes() {
        File file = builder.getClasspath();
        File net = new File(file + File.separator + "net");
        assertTrue(net.toString() + " does not exist", net.exists());
    }

    @Test
    public void getCommand() {
        List<String> command = Arrays.asList(builder.getCommand());
        assertTrue(command.contains("java"));
        assertTrue(command + "does not contain classpath",command.contains(builder.getClasspath().toString()));
        assertTrue(command.contains(Main.class.getCanonicalName()));
    }
}
