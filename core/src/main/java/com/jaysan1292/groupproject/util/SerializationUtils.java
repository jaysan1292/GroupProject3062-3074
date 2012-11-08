package com.jaysan1292.groupproject.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/** @author Jason Recillo */
public class SerializationUtils {
    private static final ObjectMapper mapper;

    private SerializationUtils() {}

    static {
        mapper = new ObjectMapper();
    }

    public static String serialize(Object obj) {
        return serialize(obj, false);
    }

    public static String serialize(Object obj, boolean indent) {
        try {
            if (indent) {
                String out;
                enableJsonIndentation(true);
                out = mapper.writeValueAsString(obj);
                enableJsonIndentation(false);
                return out;
            } else {
                return mapper.writeValueAsString(obj);
            }
        } catch (IOException e) {
            return "";
        }
    }

    private static void enableJsonIndentation(boolean indent) {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, indent);
    }
}
