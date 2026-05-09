import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

import com.viveka01.RouterTwo;
import com.viveka01.format.HttpFormat;
import com.viveka01.format.Method;

public class RouterTwoTest {

    // @Before
    // public void setup() {
    //     RouterTwo.clearRoutes(); // you need to add this method
    // }

    static class TestHandler {
        static HttpFormat.Response handle(HttpFormat.Request req) {
            return new HttpFormat.Response(
                200,
                null,
                new byte[]{'O','K'}
            );
        }
    }

    private HttpFormat.Request buildRequest(String path) throws Exception {
        String raw =
            "GET " + path + " HTTP/1.1\r\n" +
            "Host: localhost\r\n" +
            "Content-Length: 0\r\n\r\n";

        InputStream in = new ByteArrayInputStream(raw.getBytes());

        return new HttpFormat.Request(in);
    }

    @Test
    public void testRouteExecutionSuccess() throws Exception {

        RouterTwo.addRoute("/test", Method.GET, TestHandler::handle);

        HttpFormat.Request request = buildRequest("/test");

        HttpFormat.Response response =
            RouterTwo.createResponse(request);

        assertNotNull(response);

        byte[] output = response.getResponse();
        String result = new String(output);

        // check HTTP status line exists
        assertTrue(result.contains("200 OK"));
    }

    @Test
    public void testUnknownRouteReturnsServerError() throws Exception {

        HttpFormat.Request request = buildRequest("/invalid");

        HttpFormat.Response response =
            RouterTwo.createResponse(request);

        assertNotNull(response);

        String result = new String(response.getResponse());

        assertTrue(
            result.contains("500") ||
            result.contains("Internal Server Error")
        );
    }
}