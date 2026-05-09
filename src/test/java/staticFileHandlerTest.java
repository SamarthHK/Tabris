import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

import com.viveka01.RouterTwo;
import com.viveka01.format.HttpFormat;
import com.viveka01.format.Method;
import com.viveka01.logic.staticFileHandler;

public class staticFileHandlerTest {
    @Test
    public void test(){
        String header = "GET /kaworu.gif HTTP/1.1\r\n" + //
                        "Host: localhost\r\n" + //
                        "Content-Length: 0\r\n" + //
                        "\r\n";
        byte[] headerByte = header.getBytes();
        InputStream read = new ByteArrayInputStream(headerByte);
        try {
            HttpFormat.Request req = new HttpFormat.Request(read);
            req.printPacket();
            System.out.write(staticFileHandler.getFrontEndPage(req).getResponse());
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
