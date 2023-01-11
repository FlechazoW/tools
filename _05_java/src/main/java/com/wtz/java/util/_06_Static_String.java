package com.wtz.java.util;

public class _06_Static_String {

    protected static String STR = "xxx %s";

    public static void main(String[] args) {

        STR = String.format(STR, "ddd");

        System.out.println(STR);
    }
}
