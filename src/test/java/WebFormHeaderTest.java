import java.nio.charset.StandardCharsets;

import org.junit.Test;
import com.viveka01.format.*;
import com.viveka01.middleware.Element;

public class WebFormHeaderTest {
    @Test
    public void testHeader(){
        String testString =
            "Content-Disposition: form-data; name=\"Resume\"; filename=\"Resume.pdf\"\r\n" +
            "Content-Type: application/pdf";
        byte[] test = testString.getBytes(StandardCharsets.UTF_8);
        Element element = new Element(test);
        element.printAllValues();
    }
}
