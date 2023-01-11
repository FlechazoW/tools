package com.wtz.java.util;

/**
 * @author tiezhu
 * @date 2022/2/10
 */
public class _01_Common {
    // Add common util.
    public static void main(String[] args) {
        System.out.println(getClassName());
    }

    private static String getClassName() {
        return _01_Common.class.getSimpleName();
    }
}
