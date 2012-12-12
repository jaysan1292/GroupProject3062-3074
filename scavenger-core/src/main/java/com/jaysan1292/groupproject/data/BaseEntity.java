package com.jaysan1292.groupproject.data;

import org.apache.commons.lang3.StringUtils;

/** @author Jason Recillo */
public abstract class BaseEntity extends JSONSerializable {
    public abstract long getId();

    public abstract void setId(long id);

    //    @Transient
    public String getDescription() {
        return StringUtils.abbreviate(description(), 40);
    }

    /**
     * This method does nothing. It is here so that the description (a
     * dynamically-generated string) can be serialized with the object.
     */
    public void setDescription(String description) {
    }

    public abstract String description();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}
