package net.rambaldi.http.rack;

import net.rambaldi.http.HttpRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class EnvironmentTest {

    @Test
    public void can_create() {
        assertNotNull(Environment.builder().build());
    }

    @Test
    public void request_defaults_to_valid() {
        assertNotNull(Environment.builder().build().request);
    }

    @Test
    public void uses_request_from_builder() {
        HttpRequest request = HttpRequest.builder().build();
        Environment environment = Environment.builder().request(request).build();
        assertSame(request, environment.request);
    }

    @Test(expected = NullPointerException.class)
    public void request_must_not_be_null() {
        Environment.builder().request(null);
    }

    @Test
    public void input_stream_defaults_to_valid() {
        assertNotNull(Environment.builder().build().inputStream);
    }

    @Test
    public void uses_input_stream_from_builder() {
        InputStream inputStream = new InputStream();
        Environment environment = Environment.builder().inputStream(inputStream).build();
        assertSame(inputStream, environment.inputStream);
    }

    @Test(expected = NullPointerException.class)
    public void input_stream_must_not_be_null() {
        Environment.builder().inputStream(null);
    }

    @Test
    public void error_stream_defaults_to_valid() {
        assertNotNull(Environment.builder().build().errorStream);
    }

    @Test
    public void uses_error_stream_from_builder() {
        ErrorStream errorStream = new ErrorStream();
        Environment environment = Environment.builder().errorStream(errorStream).build();
        assertSame(errorStream, environment.errorStream);
    }

    @Test(expected = NullPointerException.class)
    public void error_stream_must_not_be_null() {
        Environment.builder().errorStream(null);
    }

    @Test
    public void session_defaults_to_valid() {
        assertNotNull(Environment.builder().build().session);
    }

    @Test
    public void uses_session_from_builder() {
        Session session = new Session();
        Environment environment = Environment.builder().session(session).build();
        assertSame(session, environment.session);
    }

    @Test(expected = NullPointerException.class)
    public void session_must_not_be_null() {
        Environment.builder().session(null);
    }

    @Test
    public void logger_defaults_to_valid() {
        assertNotNull(Environment.builder().build().logger);
    }

    @Test
    public void uses_logger_from_builder() {
        Logger logger = new Logger();
        Environment environment = Environment.builder().logger(logger).build();
        assertSame(logger, environment.logger);
    }

    @Test(expected = NullPointerException.class)
    public void logger_must_not_be_null() {
        Environment.builder().logger(null);
    }

    @Test
    public void version_defaults_to_valid() {
        assertNotNull(Environment.builder().build().version);
    }

    @Test
    public void uses_version_from_builder() {
        Version version = new Version();
        Environment environment = Environment.builder().version(version).build();
        assertSame(version, environment.version);
    }

    @Test(expected = NullPointerException.class)
    public void version_must_not_be_null() {
        Environment.builder().version(null);
    }

    @Test
    public void query_string_should_be_taken_from_request() {
        HttpRequest request = HttpRequest.builder().params("name","Fred").build();
        Environment environment = Environment.builder().request(request).build();
        assertEquals("name=Fred", environment.queryString);
    }
}
