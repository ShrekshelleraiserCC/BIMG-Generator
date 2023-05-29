package com.masongulu.utils;

public interface IValueChange<T> {
    void update(T oldValue, T newValue);
}
