package com.wtz.java.sink;

import java.util.List;

/**
 * @author tiezhu
 * @date 2022/2/17
 */
public interface Sink {
    void sink(List<String> list);

    void stop();
}
