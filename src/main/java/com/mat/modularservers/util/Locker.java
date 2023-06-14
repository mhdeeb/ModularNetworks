package com.mat.modularservers.util;

public class Locker {
    boolean locked = false, breakBoolean=false;
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

    public boolean breakPoint() throws InterruptedException {
        synchronized (this) {
            while (locked) wait();
        }
        return breakBoolean;
    }

    public void lock() {
        locked = true;
    }

    public void setBreak(boolean newValue) {
        breakBoolean = newValue;
    }
}
