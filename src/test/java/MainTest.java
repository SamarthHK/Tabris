import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import com.viveka01.format.*;
import com.viveka01.logic.*;

import org.codehaus.plexus.util.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class MainTest {
    public void testRequestParsing(){
        File testImage = new File("src\\test\\java\\testimage\\kaoru.png");
        try (FileInputStream read = new FileInputStream(testImage)){
            byte[] image;
            image = read.readAllBytes();
            System.out.println(ImageReciever.storeImage(ContentType.PNG, image));
            System.out.println(ImageReciever.storeImage(ContentType.PNG, image));
            System.out.println(ImageReciever.storeImage(ContentType.PNG, image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        testImage = new File("src\\test\\java\\testimage\\kaworu.gif");
        try (FileInputStream read = new FileInputStream(testImage)){
            byte[] image;
            image = read.readAllBytes();
            ImageReciever.storeImage(ContentType.GIF, image);
            ImageReciever.storeImage(ContentType.GIF, image);
            ImageReciever.storeImage(ContentType.GIF, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File controlImage = new File("src\\test\\java\\testimage\\controll.jpg");
        try {
            Assert.assertTrue(FileUtils.contentEquals(testImage, controlImage));
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}