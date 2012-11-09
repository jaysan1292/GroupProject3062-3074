package com.jaysan1292.groupproject.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/** @author Jason Recillo */
public class JsonMap extends LinkedHashMap<String, Object> implements Iterable<Map.Entry<String, Object>> {
    private ObjectMapper mapper;

    public JsonMap() {
        mapper = new ObjectMapper();
    }

    public JsonMap(boolean indent) {
        this();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, indent);
    }

    public JsonMap(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonMap(int initialCapacity, int loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public JsonMap(Map<String, Object> map) {
        super(map);
    }

    public JsonMap(String json) throws IOException {
        this();
        this.putAll(mapper.<Map<String, Object>>readValue(json, MapType.construct(LinkedHashMap.class, SimpleType.construct(String.class), SimpleType.construct(Object.class))));
    }

    public String serialize() {
        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            return "{\"error\":\"There was an error creating the JSON object.\"}";
        }
    }

    public String getString(String key) {
        Object o = get(key);
        return (o == null) ? null : o.toString();
    }

    public boolean getBoolean(String key) {
        Object o = get(key);
        if ((o != null) && (o instanceof Number)) {
            o = BooleanUtils.toBoolean(Integer.valueOf(o.toString()));
        } else if ((o != null) && !(o instanceof Boolean)) {
            o = BooleanUtils.toBoolean(o.toString());
        }
        return (o != null) && (Boolean) o;
    }

    public byte getByte(String key) {
        Object o = get(key);
        if ((o != null) && !(o instanceof Number)) {
            o = Byte.decode(o.toString());
        }
        return (o == null) ? 0 : ((Number) o).byteValue();
    }

    public short getShort(String key) {
        Object o = get(key);
        if ((o != null) && !(o instanceof Number)) {
            o = Short.decode(o.toString());
        }
        return (o == null) ? 0 : ((Number) o).shortValue();
    }

    public int getInt(String key) {
        Object o = get(key);
        if ((o != null) && !(o instanceof Number)) {
            o = Integer.decode(o.toString());
        }
        return (o == null) ? 0 : ((Number) o).intValue();
    }

    private static final Pattern longPattern = Pattern.compile("[Ll]");

    public long getLong(String key) {
        Object o = get(key);
        if ((o != null) && !(o instanceof Number)) {
            try {
                o = Long.decode(longPattern.matcher(o.toString()).replaceAll(""));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return (o == null) ? 0 : ((Number) o).longValue();
    }

    public float getFloat(String key) {
        Object o = get(key);
        if ((o != null) && !(o instanceof Number)) {
            o = Float.parseFloat(o.toString());
        }
        return (o == null) ? 0 : ((Number) o).floatValue();
    }

    public double getDouble(String key) {
        Object o = get(key);
        if ((o != null) && !(o instanceof Number)) {
            o = Double.parseDouble(o.toString());
        }
        return (o == null) ? 0 : ((Number) o).doubleValue();
    }

    public byte[] getBytes(String key) {
        return (byte[]) get(key);
    }

    public Date getDate(String key) {
        try {
            return (Date) get(key);
        } catch (ClassCastException e) {
            return new Date(getLong(key));
        }
    }

    public Iterator<Map.Entry<String, Object>> iterator() {
        return super.entrySet().iterator();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
