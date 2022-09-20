package com.masongulu.utils;

import java.util.ArrayList;
import java.util.Objects;

public class Property<T> {
    private T val;
    private final ArrayList<IValueChange<T>> listeners = new ArrayList<>();

    public Property() {
    }

    public Property(T val) {
        this.val = val;
    }

    public T get() {
        return val;
    }

    public void set(T val) {
        if (!Objects.equals(val, this.val)) {
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

