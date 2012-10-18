package tests.acceptance.ionic;

import com.asynchrony.ionicmobile.User;
import net.rambaldi.http.*;
import net.rambaldi.json.Json;
import net.rambaldi.process.FakeInputStream;
import net.rambaldi.process.FakeOutputStream;
import net.rambaldi.process.SimpleContext;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static net.rambaldi.http.HttpRequest.Method.GET;
import static net.rambaldi.http.HttpResponse.Status.Created;
import static org.junit.Assert.assertEquals;

public class Company_Information_Test {

    User[] allUsers = new User[] { newUser(), newUser() };


    private User newUser() {
        User user = new User();
        int address = System.identityHashCode(user);
        user.email = address + "@example.com";
        user.username = "joe" + address;
        return user;
    }

    @Test
    public void I_should_be_able_to_list_all_the_users() throws Exception {
        HttpResponse response = get("users.json");
        assertEquals(Created, response.status);
        User[] users = parse(response.content);
        assertEquals(allUsers,users);
    }

    private User[] parse(String content) {
        return new Json<>(User[].class).parse(content);
    }

    HttpResponse get(String url) throws Exception {
        return request(GET,url,"");
    }

    HttpResponse request(HttpRequest.Method method, String url, String content) throws Exception {
        byte[] requestBytes = HttpRequest.builder()
                .method(method)
                .resource(url)
                .content(content)
                .build().toString().getBytes();
        FakeInputStream in = new FakeInputStream(requestBytes);
        FakeOutputStream out = new FakeOutputStream();
        HttpTransactionProcessor processor = new SimpleHttpTransactionProcessor(new HttpRequestEchoProcessor(), new SimpleContext());
        SimpleHttpConnectionHandler handler = new SimpleHttpConnectionHandler(processor);
        SimpleHttpConnection connection = new SimpleHttpConnection(in,out);
        handler.handle(connection);
        HttpResponseReader reader = new HttpResponseReader(new ByteArrayInputStream(out.toByteArray()));
        return reader.take();
    }

}
