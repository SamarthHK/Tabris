import org.junit.Test;
import com.viveka01.format.*;

public class WebFormHeaderTest {
    @Test
    public void testHeader(){
        String testString = "Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryabc123";
        Request request = Request.testRequest();
        request.getHeaders(testString);
        request.printRequestParams();
    }
}
