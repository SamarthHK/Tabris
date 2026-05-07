import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.viveka01.Main;
import com.viveka01.format.HttpFormat;

public class MainTest {
    @Test
    public void testRequestParsing(){
        byte[] request = (
        "POST /api/data HTTP/1.1\r\n" +
        "Host: example.com\r\n" +
        "Content-Type: application/json\r\n" +
        "Content-Length: 17\r\n" +
        "\r\n" +
        "{\"x\":10,\"y\":20}"
        ).getBytes(java.nio.charset.StandardCharsets.US_ASCII);
        InputStream in = new ByteArrayInputStream(request);
        try {
            HttpFormat.Request test = new HttpFormat.Request(in);
            test.printRequestParams();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}