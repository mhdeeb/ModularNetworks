package com.mat.modularservers.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class LinkedFixedList<T> implements Iterable<T> {
    public final int capacity;
    private Node<T> head, tail;
    private int size = 0;

    public LinkedFixedList(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException();
        head = tail = null;
        this.capacity = capacity;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            Node<T> i = head;

            @Override
            public boolean hasNext() {
                return i != null;
            }

            @Override
            public T next() {
                Node<T> perv = i;
                i = i.next;
                return perv.item;
            }
        };
    }

    static class Node<E> {
        E item;
        Node<E> next = null;

        Node(E x) {
            item = x;
        }
    }

    public int size() {
        return size;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (Node<T> x = head; x != null; x = x.next)
            stringBuilder.append(x.item).append(',').append(' ');
        stringBuilder.append('\b').append('\b').append(']');
        return stringBuilder.toString();
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<T> x = head; x != null; x = x.next)
            result[i++] = x.item;
        return result;
    }

    public void fixedInsert(T item) {
        if (head != null) {
            tail = tail.next = new Node<>(item);
            if (size == capacity) {
                head.item = null;
                head = head.next;
            } else size++;
        } else {
            head = tail = new Node<>(item);
            size++;
        }
    }

    public Long sum() {
        long total = 0;
        for (Node<T> x = head; x != null; x = x.next)
            if(x.item instanceof Long r) total += r;
        return total;
    }
}
