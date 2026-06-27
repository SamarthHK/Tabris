import com.viveka01.format.http.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class staticFileHandlerTest {
    public void test(){
        String header = "GET /kaworu.gif HTTP/1.1\r\n" + //
                        "Host: localhost\r\n" + //
                        "Content-Length: 0\r\n" + //
                        "\r\n";
        byte[] headerByte = header.getBytes();
        InputStream read = new ByteArrayInputStream(headerByte);
        try {
            Request req = new Request(read);
            // req.printPacket();
            // System.out.write(staticFileHandler.getFrontEndPage(req).getResponse());
            // System.out.println();
            // RouterOld.createResponse(req).getResponse();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
