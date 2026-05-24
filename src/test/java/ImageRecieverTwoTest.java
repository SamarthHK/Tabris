import java.io.IOException;

import org.junit.Test;

import com.viveka01.logic.ImageRecieverTwo;

public class ImageRecieverTwoTest {
    @Test
    public void singleFileHandleTest(){
        ImageRecieverTwo.singleFileHandle("Hello!!!", new byte[0]).getResponse();
    }
}
