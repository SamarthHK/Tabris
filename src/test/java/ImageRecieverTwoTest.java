import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class ImageRecieverTwoTest {
    @Test
    public void singleFileHandleTest(){
        ImageRecieverTwo.singleFileHandle("Dihhhhh.com","FileContentsYK".getBytes(StandardCharsets.UTF_8));
    }
}
