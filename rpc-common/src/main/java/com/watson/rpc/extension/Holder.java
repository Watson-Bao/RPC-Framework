package com.watson.rpc.extension;

/**
 * Holder，顾名思义，用于持有目标对象
 * @author watson
 */
public class Holder<T> {
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
