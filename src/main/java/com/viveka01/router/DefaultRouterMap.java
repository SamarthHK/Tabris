package com.viveka01.router;

import com.viveka01.Main;
import com.viveka01.format.Initialize;
import com.viveka01.format.Method;
import com.viveka01.logic.CorsAccept;
import com.viveka01.logic.ImageReceiver;
import com.viveka01.logic.ImageReceiver;
import com.viveka01.logic.StaticFileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRouterMap implements Initialize {
    static final Logger LOGGER = LoggerFactory.getLogger(DefaultRouterMap.class);
    static String routerName = "com.viveka01.router.RouterMap";

    /**
     * Adding all the routes
     * @throws ClassNotFoundException If somehow you added a route and the class is missing this is thrown
     */
    public static void init() throws ClassNotFoundException{
        addRoutes();
    }

    private static void addRoutes(){
        RouterMap.addRoute(Method.GET, "/",StaticFileHandler::getFrontEndPage);
        RouterMap.addRoute(Method.GET, "/{f}",StaticFileHandler::getFrontEndPage);
        RouterMap.addRoute(Method.GET, "/getStaticFile/{f}",StaticFileHandler::getFrontEndPage);
        RouterMap.addRoute(Method.GET, "/viewImage/{*}",ImageReceiver::getImage);
        RouterMap.addRoute(Method.POST, "/upload", ImageReceiver::storeImage);
        RouterMap.addRoute(Method.OPTIONS, "/upload",CorsAccept::handleCors);
    }
}
