import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
        final String body = "src\\test\\java\\testPacket\\element1.raw";
        final String boundary = "----WebKitFormBoundaryeeg2XBlrfU8uwkMt";

        File packetFile = new File(packet);
        File bodyFile = new File(body);
        byte[] packetByte;
        byte[] bodyByte;
        try{
            FileInputStream read = new FileInputStream(packetFile); 
            packetByte = read.readAllBytes();
            read = new FileInputStream(bodyFile);
            bodyByte = read.readAllBytes();
            read.close();
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        WebFormParser webFormParser = new WebFormParser(boundary, packetByte);
        webFormParser.detectSubArrayIndex(packetByte, boundary.getBytes(StandardCharsets.UTF_8));
    }
}
