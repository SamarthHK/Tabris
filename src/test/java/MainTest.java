import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import com.viveka01.format.HttpFormat;

public class MainTest {
    @Test
    public void testRequestParsing(){
        byte[] request = (
            "POST /api/v1/users/create HTTP/1.1\r\n" +
            "Host: api.example.com:8080\r\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)\r\n" +
            "Accept: application/json,text/plain,*/*\r\n" +
            "Accept-Language: en-US,en;q=0.9\r\n" +
            "Accept-Encoding: gzip, deflate\r\n" +
            "Connection: keep-alive\r\n" +
            "Cache-Control: no-cache\r\n" +
            "Authorization: Bearer ABC123SUPERLONGTOKENXYZ\r\n" +
            "X-Forwarded-For: 192.168.1.10\r\n" +
            "X-Request-ID: req-123456789\r\n" +
            "Content-Type: application/json\r\n" +
            "Content-Length: 252\r\n" +
            "\r\n" +
            "{\"user\":{\"username\":\"samarth\",\"password\":\"supersecretpassword\",\"email\":\"samarth@example.com\",\"roles\":[\"admin\",\"developer\",\"tester\"],\"profile\":{\"firstName\":\"Samarth\",\"lastName\":\"Kumar\",\"age\":15,\"bio\":\"Testing large HTTP parser request body handling.\"}}}"
        ).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(request);
        try {
            HttpFormat.Request test = new HttpFormat.Request(in);
            System.out.printf("Amount of bytes to read: %d\n",request.length);
            System.out.println();
            test.printPacket();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}