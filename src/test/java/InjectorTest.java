import com.viveka01.Main;
import com.viveka01.format.fileInjector.*;
import com.viveka01.format.json.JsonObjectMapper;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class InjectorTest {
    private static String passage;
    private static String testFile = "test.html";
    static{
        init();
    }

    @Test
    public void test(){
        init();
        ArrayList<InjectionLocation> locations = InjectionLocator.getFileInjectionLocation(testFile);
        assert(!locations.isEmpty());
        HandleStaticFiles test;
        try {
            test = new HandleStaticFiles(testFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Passage:\n"+test.getFileString()+"\n");
        for(InjectionLocation location : locations){
            System.out.printf("Tag: %s, Start: %d, End: %d\n",location.type().toString(),location.start(),location.end());
        }
        System.out.println("");
        Injector injector = new Injector(test.getFileString(),locations);
        injector.replaceTemplate("WORDWORD", InjectionType.TITLE,0);
        injector.replaceTemplate("WORDWORD", InjectionType.LINK,0);
        injector.replaceTemplate("WORDWORD", InjectionType.IMAGE,0);
        System.out.println("Passage After:\n"+injector.getPassage()+"\n");
    }
    static private void init(){
        try{
            Main.init();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
