package com.jaysan1292.groupproject.exceptions;

import com.jaysan1292.groupproject.data.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {
        try {
            String message = String.format("The %s could not be found, or a connection to the database could not be made.", determineItemType().toLowerCase());
            FieldUtils.writeField(this, "detailMessage", message, true);
        } catch (IllegalAccessException ignored) {}
    }

    public ItemNotFoundException(long id) {
        try {
            String message = String.format("Could not find any %s with the ID: %d", determineItemType(), id);
            FieldUtils.writeField(this, "detailMessage", message, true);
        } catch (IllegalAccessException ignored) {}
    }

    public <T extends BaseEntity> ItemNotFoundException(long id, Class<T> itemClass) {
        this(id, itemClass, null);
    }

    public <T extends BaseEntity> ItemNotFoundException(long id, Class<T> itemClass, Throwable cause) {
        super(cause);
        try {
            String message = String.format("Could not find any %s with the ID: %d", extractTypeName(itemClass.getName()), id);
            FieldUtils.writeField(this, "detailMessage", message, true);
        } catch (IllegalAccessException ignored) {}
    }

    public ItemNotFoundException(String format, Object... args) {
        try {
            String message = String.format(format, args);
            FieldUtils.writeField(this, "detailMessage", message, true);
        } catch (IllegalAccessException ignored) {}
    }

    private String determineItemType() {
        String callingClass = getStackTrace()[0].getClassName();
        return extractTypeName(callingClass);
    }

    private static String extractTypeName(String className) {
        String[] name = StringUtils.split(className, '.');
        return StringUtils.splitByCharacterTypeCamelCase(name[name.length - 1])[0].toLowerCase();
    }
}
