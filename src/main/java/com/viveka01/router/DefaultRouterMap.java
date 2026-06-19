package com.viveka01.router;

import com.viveka01.Main;
import com.viveka01.format.Method;
import com.viveka01.logic.CorsAccept;
import com.viveka01.logic.ImageReceiver;
import com.viveka01.logic.ImageReceiver;
import com.viveka01.logic.StaticFileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRouterMap {
    static final Logger LOGGER = LoggerFactory.getLogger(DefaultRouterMap.class);
    static String routerName = "com.viveka01.router.RouterMap";
    static{
        System.out.println("Initializing Router");
        try {
            LOGGER.info("Called static block of {}",Class.forName(routerName));
            addRoutes();
            LOGGER.info("Added routes");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Failed to call static block of {}",routerName,e);
        }
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
