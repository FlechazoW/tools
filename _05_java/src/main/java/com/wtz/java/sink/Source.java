package com.wtz.java.sink;

import java.util.List;

/**
 * @author tiezhu
 * @date 2022/2/17
 */
public interface Source {
    void source(List<String> list);

    void stop();
}
