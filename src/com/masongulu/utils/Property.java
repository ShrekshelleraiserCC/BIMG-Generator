package com.masongulu.utils;

import java.util.ArrayList;

public class Property <T> {
    private T val;
    private final ArrayList<IValueChange<T>> listeners = new ArrayList<>();

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        if (val != this.val) {
            for (IValueChange<T> listener : listeners) {
                listener.update(this.val, val);
            }
            this.val = val;
        }
    }

    public void addListener(IValueChange<T> listener) {
        listeners.add(listener);
    }
}

interface IValueChange<T> {
    void update(T oldValue, T newValue);
}