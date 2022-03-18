package com.wtz.java.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class UnionIterator<T> implements Iterator<T>, Iterable<T> {

    private Iterator<T> currentIterator;

    private ArrayList<Iterator<T>> furtherIterators = new ArrayList<>();

    private int nextIterator;

    private boolean iteratorAvailable = true;

    // ------------------------------------------------------------------------

    public void clear() {
        currentIterator = null;
        furtherIterators.clear();
        nextIterator = 0;
        iteratorAvailable = true;
    }

    public void addList(List<T> list) {
        add(list.iterator());
    }

    public void add(Iterator<T> iterator) {
        if (currentIterator == null) {
            currentIterator = iterator;
        } else {
            furtherIterators.add(iterator);
        }
    }

    // ------------------------------------------------------------------------

    @Override
    public Iterator<T> iterator() {
        if (iteratorAvailable) {
            iteratorAvailable = false;
            return this;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean hasNext() {
        while (currentIterator != null) {
            if (currentIterator.hasNext()) {
                return true;
            } else if (nextIterator < furtherIterators.size()) {
                currentIterator = furtherIterators.get(nextIterator);
                nextIterator++;
            } else {
                currentIterator = null;
            }
        }

        return false;
    }

    @Override
    public T next() {
        if (hasNext()) {
            return currentIterator.next();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}