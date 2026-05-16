package com.viveka01.router;

public class DefaultRouterMap {
    static String routerName = "com.viveka01.router.RouterMap";
    static{
        System.out.println("Creating Initializing Router");
        try {
            System.out.printf("Called static block of %s\n",Class.forName(routerName));
        } catch (ClassNotFoundException e) {
            System.out.printf("Failed to call static block of %s\n",routerName);
            e.printStackTrace();
        }
    }
}
