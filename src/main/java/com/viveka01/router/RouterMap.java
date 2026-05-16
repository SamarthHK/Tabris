package com.viveka01.router;

import com.viveka01.format.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RouterMap {

    private static Map<Method,TreeNode> roots = new ConcurrentHashMap<>();
    static{
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
        TreeNode node = traverseTree(method, route);
        return node.getHandle();
    }

    static private TreeNode traverseTree(Method method, String route) throws NullPointerException{
        System.out.println("DebugStatements:");
        String[] routeSegments = route.split("/");
        TreeNode node = roots.get(method);
        for(String segment: routeSegments){
            if (!node.containsChild(segment)){
                node.addChild(new TreeNode(segment));
            }
            node = node.getChild(segment);
        }
        return node;
    } 
    
}
    

