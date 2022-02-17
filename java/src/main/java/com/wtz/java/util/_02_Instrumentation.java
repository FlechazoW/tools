package com.wtz.java.util;

import java.lang.instrument.Instrumentation;

/**
 * @author tiezhu
 * @date 2022/2/15
 * <p>
 * 需要用java agent，添加到java main
 */
public class _02_Instrumentation {

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object obj) {
        return instrumentation.getObjectSize(obj);
    }
}
