import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.codehaus.plexus.util.FileUtils;
import org.junit.Test;
import com.viveka01.format.*;
import com.viveka01.middleware.Element;
import com.viveka01.middleware.WebFormParser;

public class WebFormHeaderTest {
    
    public void testHeader(){
        final String readFile = "src\\test\\java\\testPacket\\element1.raw";
        final String writeDir = "src\\test\\java\\testimage\\";

        byte[] test;
        try {
            FileInputStream read = new FileInputStream(readFile);
            test = read.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("\n");
        Element element = new Element(test);
        element.printAllValues();

        byte[] fileByte = element.getBody();
        String fileName = element.getFileName();

        try{
            File writeFile = new File(writeDir+fileName);
            FileOutputStream write = new FileOutputStream(writeFile);
            if (writeFile.exists()){
                writeFile.delete();
            }
            writeFile.createNewFile();
            write.write(fileByte);
            System.out.println("Header bytes: "+Arrays.toString(Arrays.copyOfRange(fileByte, 0, 10)));
            System.out.println("Created the file "+fileName);
        }catch (IOException e){
            e.printStackTrace();
        }
        
    }
    @Test
    public void testDetectSubArrayIndex(){
        final String packet = "src\\test\\java\\testPacket\\debug.raw";
        final String boundary = "------WebKitFormBoundarybBce1CgXAUwK2vz1";
        final String writeDir = "src\\test\\java\\testimage\\";
        final String control = "src\\test\\java\\testimage\\control.jpg";

        File packetFile = new File(packet);
        byte[] packetByte;
        try{
            FileInputStream read = new FileInputStream(packetFile); 
            packetByte = read.readAllBytes();
            read.close();
        }catch (IOException e){
            e.printStackTrace();
            return;
        }

        WebFormParser webFormParser = new WebFormParser(boundary, packetByte);
        Element element = webFormParser.getElements().get(0);

        String fileName = element.getFileName();
        byte[] body = element.getBody();
        File img = new File(writeDir+fileName); 
        if (img.exists()){
            img.delete();
        }
        try{
            img.createNewFile();
            FileOutputStream write = new FileOutputStream(img);
            write.write(body);
            write.close();
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        System.out.println("Created file sucessfully!!!");
        try {
            if (FileUtils.contentEquals(new File(control), img)){
                System.out.println("Both files are same");
            }
            else{
                System.out.println("Files are not equal");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
