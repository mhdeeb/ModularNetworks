package com.mat.modularservers.util;

public class Locker {
    boolean locked = false;

    public void unlock() {
        synchronized (this) {
            locked = false;
            notify();
        }
    }

    public void toggle() {
        synchronized (this) {
            locked = !locked;
            notify();
        }
    }

    public void breakPoint() throws InterruptedException {
        synchronized (this) {
            while (locked) wait();
        }
    }

    public void lock() {
        locked = true;
    }
}
