import java.nio.charset.StandardCharsets;

import org.junit.Test;
import com.viveka01.format.*;
import com.viveka01.middleware.Element;

public class WebFormHeaderTest {
    @Test
    public void testHeader(){
        String testString =  """
        Content-Disposition: form-data; name="resume"; filename="resume.pdf"
        Content-Type: application/pdf
        """;
        byte[] test = testString.getBytes(StandardCharsets.UTF_8);
        Element element = new Element(test);
        element.printAllValues();
    }
}
