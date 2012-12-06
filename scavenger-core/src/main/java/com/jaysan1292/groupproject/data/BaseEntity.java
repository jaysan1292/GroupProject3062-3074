package com.jaysan1292.groupproject.data;

/** @author Jason Recillo */
public abstract class BaseEntity extends JSONSerializable {
    public abstract long getId();

    public abstract void setId(long id);

    public abstract String getDescription();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}
