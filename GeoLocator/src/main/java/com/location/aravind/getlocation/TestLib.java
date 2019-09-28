package com.location.aravind.getlocation;

public class TestLib {
    public static String test(String str){
        String addition = "==test add==";
        if (str==null){
            str = addition;
        } else {
            str+= addition;
        }
        return str;
    }
}
