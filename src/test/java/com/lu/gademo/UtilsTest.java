package com.lu.gademo;


import com.lu.gademo.utils.impl.UtilImpl;
import org.junit.jupiter.api.Test;

public class UtilsTest {
    @Test
    public void testConda() {
        UtilImpl util = new UtilImpl();

        if (util.isCondaInstalled()) {
            System.out.println("conda installed on this machine");
        } else {
            System.out.println("conda not installed");
        }
    }
}
