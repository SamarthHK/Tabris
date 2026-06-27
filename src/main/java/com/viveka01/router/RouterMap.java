package com.viveka01.router;

import com.viveka01.format.http.Method;
import com.viveka01.format.http.Request;
import com.viveka01.format.http.Response;
import com.viveka01.logic.ServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RouterMap {
    static final Logger LOGGER = LoggerFactory.getLogger(RouterMap.class);
    static TreeNode SERVER_ERROR = new TreeNode("SERVER_ERROR");

    private static Map<Method,TreeNode> roots = new ConcurrentHashMap<>();
    static{
        SERVER_ERROR.setHandle(ServerError::serverError);
        for(Method key : Method.values()){
            roots.put(key,new TreeNode(""));
        }
    }
    /**
     * @param method HTTP method
     * @param route route in /foo/bar format
     * @param handle the method
     */
    static public void addRoute(Method method, String route,RouteHandler handle){
        TreeNode node = addNode(method, route);
        node.setHandle(handle);
    }
    /**
     * @param method HTTP method
     * @param route route in /foo/bar format
     * @return returns the handle at the end of the tree, returns NULL
     */
    static public RouteHandler getHandle(Method method, String route){
        TreeNode node;
        try{
            node = readTree(method, route);
            return node.getHandle();
        }catch (NullPointerException e){
            LOGGER.info("Error finding a node");
            return SERVER_ERROR.getHandle();
        }
    }
    /**
     * @return returns handle corresponding to path and code, else will return SERVER_ERROR and print debug
     */
    public static Response createResponse(Request request){
        String path = request.getPath();
        Method code = request.getMethod();
        try {
            RouteHandler handler = getHandle(code, path);
            LOGGER.info("Got handle");
            return handler.handle(request);
        } catch (Exception e) {
            LOGGER.error("Error with handling");
            LOGGER.error("path: {}, code: {}",path,code.toString(),e);
        }
        return Response.SERVER_ERROR;
    }

    /**
     * @param method HTTP Method
     * @param route route in format of /foo/bar
     * @return returns node that's ends in same place as route given
     */
    static private TreeNode addNode(Method method, String route){
        String[] routeSegments = route.split("/");
        TreeNode node = roots.get(method);
        String compare = "";

        for(String segment: routeSegments){
            if (segment.isEmpty()) continue;
            compare = segment;
            if (Id.getType(compare) != Id.STRING){
                compare = Id.getType(compare).code;
            }
            if (!node.containsChild(compare)){
                node.addChild(new TreeNode(segment));
            }
            node = node.getChild(compare);
        }
        return node;
    }

    /**
     *
     * @param method The http method used to access the site, DELETE, GET, PUT, HEAD
     * @param route Request route, example /upload or /viewImage/...
     * @return a TreeNode object that contains the handler/ endpoint
     * @throws NullPointerException When node is not found with current path
     */
    static private TreeNode readTree(Method method,String route) throws NullPointerException{
        String[] routeSegments = route.split("/");
        TreeNode node = roots.get(method);
        String compare = "";

        for(String segment: routeSegments){
            if (segment.isEmpty()) continue;
            Id segmentType = Id.getType(segment);

            if (node.containsChild(segment)){
                node = node.getChild(segment);
                continue;
            }
            if (node.containsChild(segmentType.code)){
                node = node.getChild(segmentType.code);
                continue;
            }
            LOGGER.error("Could not find node {} {}",method,route);
            throw new NullPointerException("Node not found");
        }
        return node;
    }
}
    

