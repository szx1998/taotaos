package com.taotao.portal.mytest;

import org.junit.jupiter.api.Test;

public class MyTest {

    @Test
    public void show(){
        String s = "1";
        int i = Integer.parseInt(s);
        i += 1;
        String ss = Integer.toString(i);
        Integer ii = 60*60*24*7;
        System.out.println(ii);
        System.out.println(ss);
    }
}
