import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.viveka01.logic.ImageRecieverTwo;

public class ImageRecieverTwoTest {
    @Test
    public void singleFileHandleTest(){
        ImageRecieverTwo.singleFileHandle("Dihhhhh.com","FileContentsYK".getBytes(StandardCharsets.UTF_8));
    }
}
