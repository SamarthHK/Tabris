package com.viveka01.router;

import com.viveka01.format.*;
import com.viveka01.logic.ServerError;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RouterMap {
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
        TreeNode node = traverseTree(method, route);
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
            node = traverseTree(method, route);
            return node.getHandle();
        }catch (Exception e){
            return SERVER_ERROR.getHandle();
        }
    }
    /**
     * @return returns handle coresponsing to path and code, else will return SERVER_ERROR and print debug
     */
    public static Response createResponse(Request request){
        String path = request.getPath();
        Method code = request.getMethod();
        try {
            RouteHandler handler = getHandle(code, path);
            return handler.handle(request);
        } catch (Exception e) {
            System.out.println("Yea error....");
            System.out.printf("path: %s ,code: %s\n",path,code.toString());
            e.printStackTrace();
        }
        return Response.SERVER_ERROR;
    }

    /**
     * @param method HTTP Method
     * @param route route in format of /foo/bar
     * @return returns node thats ends in same place as route given
     * @throws NullPointerException throws this when Node cannot be found, acount for it by sending server error response
     */
    static private TreeNode traverseTree(Method method, String route){
        String[] routeSegments = route.split("/");
        TreeNode node = roots.get(method);
        String compare = "";

        for(String segment: routeSegments){
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
}
    

