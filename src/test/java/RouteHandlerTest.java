import org.junit.Test;

import com.viveka01.router.*;
import com.viveka01.format.Method;
import com.viveka01.logic.StaticFileHandler;
public class RouteHandlerTest {
    @Test
    public void testRouter(){
        try{
            Class.forName("com.viveka01.router.DefaultRouterMap");        
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        RouteHandler test = StaticFileHandler::getFrontEndPage;
        Method method = Method.GET;
        String path = "/getStaticFiles/{f}";
        RouterMap.addRoute(method,path,test);
        if (test.equals(RouterMap.getHandle(method,"/getStaticFiles/betterNotWork/hello.png"))){
            System.out.println("Both are same");
        }
        else{
            System.out.println("Both are not same");
        } 
    }
    
    public void testId(){
        System.out.println(Id.getIdFromCode("{*}"));
    }
}
