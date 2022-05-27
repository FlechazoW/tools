package com.wtz.java.util;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author tiezhu
 * @date 2022/2/15
 */
public class _02_Object_GetBytes {

    public static void main(String[] args) {
        List<String> objList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            objList.add("aaa_" + i);
        }

        String str = objList.toString();
        String str2 = String.join(", ", objList);

//        System.out.println(_02_Instrumentation.getObjectSize(objList));
        System.out.println("Size of: " + RamUsageEstimator.shallowSizeOf(objList));
        System.out.println("Size of: " + RamUsageEstimator.sizeOfCollection(objList));
        System.out.println("Size of: " + RamUsageEstimator.shallowSizeOf("aaa_1"));
        System.out.println("Size of: " + RamUsageEstimator.sizeOfCollection(Collections.singleton("aaa_1")));
        System.out.println("Size of: " + RamUsageEstimator.shallowSizeOf(str));
        System.out.println("Size of: " + RamUsageEstimator.sizeOfCollection(Collections.singleton(str)));
        System.out.println("Size of: " + RamUsageEstimator.shallowSizeOf(str2));
        System.out.println("Size of: " + RamUsageEstimator.sizeOfCollection(Collections.singleton(str2)));
        System.out.println("Size of: " + ObjectSizeCalculator.getObjectSize(objList));

        while (true) {

        }
    }
}
