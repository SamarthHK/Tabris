import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.viveka01.FileMapper;
import com.viveka01.format.*;
import com.viveka01.logic.*;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainTest {
    public void testRequestParsing(){
        File testImage = new File("src\\test\\java\\testimage\\kaoru.png");
        
        try (FileInputStream read = new FileInputStream(testImage)){
            byte[] image;
            image = read.readAllBytes();
            ImageReciever.storeImage(ContentType.PNG, image);
            ImageReciever.storeImage(ContentType.PNG, image);
            ImageReciever.storeImage(ContentType.PNG, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testFileMapper(){
        
    }
}