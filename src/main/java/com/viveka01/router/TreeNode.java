package com.viveka01.router;

import java.util.HashMap;

public class TreeNode{
    private final String key;
    private RouteHandler handle;
    
    private HashMap<String,TreeNode> children = new HashMap<>();

    public TreeNode(String key){
        this.key = key;
    }
    /**
     * @return returns the key associated with the object
     */
    public String getKey(){
        return key;
    }
    /**
     * @param handle Handle object to replace current one
     */
    public void setHandle(RouteHandler handle){
        this.handle = handle;
    }
    /**
     * @return returns handle of current object
     */
    public RouteHandler getHandle(){
        return handle;
    }
    /**
     * @param node Adds a TreeNode object to hashMap
     */
    public void addChild(TreeNode node){
        children.put(node.getKey(),node);
    }
    /**
     * @param key route segment associated with object for retrival
     * @return returns object associated with the key, NULL is returned when the object doesnt exist
     */
    public TreeNode getChild(String key){
        return children.get(key);
    }


}
