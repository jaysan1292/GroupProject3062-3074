package com.jaysan1292.groupproject.data;

/**
 * @author Jason Recillo
 */
public abstract class AbstractBuilder<T> {

    protected abstract void init();

    public abstract T build();
}
