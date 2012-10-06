package tests.acceptance;

import net.rambaldi.http.HttpRequest;
import net.rambaldi.http.rack.Environment;
import net.rambaldi.http.rack.JRubyRack;
import net.rambaldi.http.rack.Rack;
import net.rambaldi.http.rack.Response;
import org.junit.Test;

import static net.rambaldi.http.rack.Response.Body;

import net.rambaldi.http.rack.ResponseHeaders;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * See
 * http://rack.rubyforge.org/doc/SPEC.html
 * http://chneukirchen.org/talks/euruko-2007/chneukirchen-euruko2007-introducing-rack.pdf
 */
public class Rack_Test {

    @Test
    public void I_should_be_able_to_use_rack_to_serve_a_java_application() {
        Rack app = new Rack(){
            @Override
            public Response call(Environment env) {
                ResponseHeaders headers = new ResponseHeaders();
                String name = env.queryString.split("=")[1];
                Body body = new Body("Hello " + name);
                return new Response(200,headers,body);
            }
        };
        checkApp(app);
    }

    void checkApp(Rack app) {
        HttpRequest request = HttpRequest.builder().params("name","Fred").build();
        Environment env = Environment.builder().request(request).build();
        Response response = app.call(env);
        int status = 200;
        ResponseHeaders headers = new ResponseHeaders();
        Body body = new Body("Hello Fred");
        assertEquals(status,response.status);
        assertEquals(headers,response.headers);
        assertEquals(body,response.body);
    }

    @Test
    public void I_should_be_able_to_use_rack_to_serve_a_ruby_application() {
        Rack app = new JRubyRack(
            "def call(env)",
            "  req = Rack::Request.new(env)",
            "  res = Rack::Response.new",
            "  if req.get?",
            "    res.write \"Hello #{req.GET[\"name\"]}\"",
            "  end",
            "  res.finish",
            "end"
        );
        checkApp(app);
    }
}
