package com.wtz.java.util;

import java.util.Collections;
import java.util.Iterator;

public class _04_FromRangePort {
    public static void main(String[] args) {


    }

    public static Iterator<Integer> get(String rangeDefinition) {
        UnionIterator<Integer> iterators = new UnionIterator<>();
        final String[] ranges = rangeDefinition.trim().split(",");
        for (String rawRange : ranges) {
            Iterator<Integer> rangeIterator;
            String range = rawRange.trim();
            int dashIdx = range.indexOf('-');
            if (dashIdx == -1) {
                // only one port in range:
                final int port = Integer.parseInt(range);
                if (!isValidHostPort(port)) {
                    throw new RuntimeException("Invalid port configuration. Port must be between 0" +
                            "and 65535, but was " + port + ".");
                }
                rangeIterator = Collections.singleton(Integer.valueOf(range)).iterator();
            } else {
                // evaluate range
                final int start = Integer.parseInt(range.substring(0, dashIdx));
                if (!isValidHostPort(start)) {
                    throw new RuntimeException("Invalid port configuration. Port must be between 0" +
                            "and 65535, but was " + start + ".");
                }
                final int end = Integer.parseInt(range.substring(dashIdx + 1, range.length()));
                if (!isValidHostPort(end)) {
                    throw new RuntimeException("Invalid port configuration. Port must be between 0" +
                            "and 65535, but was " + end + ".");
                }
                rangeIterator = new Iterator<Integer>() {
                    int i = start;

                    @Override
                    public boolean hasNext() {
                        return i <= end;
                    }

                    @Override
                    public Integer next() {
                        return i++;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Remove not supported");
                    }
                };
            }
            iterators.add(rangeIterator);
        }
        return iterators;
    }

    /**
     * check whether the given port is in right range when getting port from local system.
     *
     * @param port the port to check
     * @return true if the number in the range 0 to 65535
     */
    public static boolean isValidHostPort(int port) {
        return 0 <= port && port <= 65535;
    }
}
