package com.viveka01;

import java.util.ArrayList;
import java.util.List;

public class RouterMap {
    public class TreeNode<T>{
        private String key;
        private T value;
        private HashMap<String,<T>> children = new HashMap<>();

        public TreeNode(T value){
            this.value = value;
        }

        public void addChild(TreeNode<T> child){
            children.add(child);
        }

        public T getValue(){
            return value;
        }

        public List<TreeNode<T>> getChildren(){
            return children;
        }
    }
}
