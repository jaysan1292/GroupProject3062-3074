package com.jaysan1292.groupproject.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** @author Jason Recillo */
@SuppressWarnings("unchecked")
public abstract class JSONSerializable {
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Reads a JSON-encoded array into an {@code ArrayList} containing its elements.
     *
     * @param cls  The class of the objects to deserialize.
     * @param json The JSON-encoded string.
     *
     * @return An {@code ArrayList} containing the objects that were encoded in the JSON array.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> ArrayList<T> readJSONArray(Class<T> cls, String json) throws IOException {
        return mapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, cls));
    }

    /**
     * Reads a JSON-encoded array into an {@code ArrayList} containing its elements.
     *
     * @param cls    The class of the objects to deserialize.
     * @param reader A {@code Reader} pointing to an input stream that contains a JSON-encoded object array.
     *
     * @return An {@code ArrayList} containing the objects that were encoded in the JSON array.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> ArrayList<T> readJSONArray(Class<T> cls, Reader reader) throws IOException {
        return mapper.readValue(reader, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, cls));
    }

    /**
     * Reads a JSON_encoded array into an {@code ArrayList} containing its elements.
     *
     * @param cls    The class of the objects to deserialize.
     * @param stream An {@code InputStream} pointing to a stream that contains a JSON-encoded object array.
     *
     * @return An {@code ArrayList} containing the objects that were encoded in the JSON array.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> ArrayList<T> readJSONArray(Class<T> cls, InputStream stream) throws IOException {
        return mapper.readValue(stream, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, cls));
    }

    /**
     * Serializes a {@code List} containing objects that extend {@code JSONSerializable} into a JSON-encoded string.
     *
     * @param list The {@code List} containing the objects to serialize.
     *
     * @return A {@code String} containing the JSON representation of the list.
     */
    public static <T extends JSONSerializable> String writeJSONArray(List<T> list) {
        return writeJSONArray(list, false);
    }

    /**
     * Serializes a {@code List} containing objects that extend {@code JSONSerializable} into a JSON-encoded string.
     *
     * @param list   The {@code List} containing the objects to serialize.
     * @param indent Whether or not to indent the output.
     *
     * @return A {@code String} containing the JSON representation of the list.
     */
    public static <T extends JSONSerializable> String writeJSONArray(List<T> list, boolean indent) {
        try {
            if (indent) {
                enableJsonIndentation(true);
                String out = mapper.writeValueAsString(list);
                enableJsonIndentation(false);
                return out;
            } else {
                return mapper.writeValueAsString(list);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            return "";
        }
    }

    /**
     * Serializes a {@code Set} containing objects that extend {@code JSONSerializable} into a JSON-encoded string.
     *
     * @param set The {@code Set} containing the objects to serialize.
     *
     * @return A {@code String} containing the JSON representation of the list.
     */
    public static <T extends JSONSerializable> String writeJSONArray(Set<T> set) {
        return writeJSONArray(set, false);
    }

    /**
     * Serializes a {@code Set} containing objects that extend {@code JSONSerializable} into a JSON-encoded string.
     *
     * @param set    The {@code Set} containing the objects to serialize.
     * @param indent Whether or not to indent the output.
     *
     * @return A {@code String} containing the JSON representation of the list.
     */
    public static <T extends JSONSerializable> String writeJSONArray(Set<T> set, boolean indent) {
        try {
            if (indent) {
                enableJsonIndentation(true);
                String out = mapper.writeValueAsString(set);
                enableJsonIndentation(false);
                return out;
            } else {
                return mapper.writeValueAsString(set);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            return "";
        }
    }

    /**
     * Serializes an array containing objects that extend {@code JSONSerializable} into a JSON-encoded string.
     *
     * @param values The values to serialize.
     *
     * @return A {@code String} containing the JSON representation of the list.
     */
    public static <T extends JSONSerializable> String writeJSONArray(T... values) {
        return writeJSONArray(false, values);
    }

    /**
     * Serializes an array containing objects that extend {@code JSONSerializable} into a JSON-encoded string.
     *
     * @param indent Whether or not to indent the output.
     * @param values The values to serialize.
     *
     * @return A {@code String} containing the JSON representation of the list.
     */
    public static <T extends JSONSerializable> String writeJSONArray(boolean indent, T... values) {
        try {
            if (indent) {
                enableJsonIndentation(true);
                String out = mapper.writeValueAsString(values);
                enableJsonIndentation(false);
                return out;
            } else {
                return mapper.writeValueAsString(values);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            return "";
        }
    }

    /**
     * Serializes a {@code List} containing objects that extend {@code JSONSerializable}, and outputs them using the specified
     * output stream.
     *
     * @param list   The {@code List} containing the objects to serialize.
     * @param writer The output stream to send the output to.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> void writeJSONArray(List<T> list, Writer writer) throws IOException {
        writeJSONArray(list, writer, false);
    }

    public static <T extends JSONSerializable> void writeJSONArray(List<T> list, Writer writer, boolean indent) throws IOException {
        try {
            if (indent) {
                enableJsonIndentation(true);
                mapper.writeValue(writer, list);
                enableJsonIndentation(false);
            } else {
                mapper.writeValue(writer, list);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            throw e;
        }
    }

    /**
     * Serializes a {@code List} containing objects that extend {@code JSONSerializable}, and outputs them using the specified
     * output stream.
     *
     * @param list    The {@code List} containing the objects to serialize.
     * @param ostream The output stream to send the output to.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> void writeJSONArray(List<T> list, OutputStream ostream) throws IOException {
        writeJSONArray(list, ostream, false);
    }

    /**
     * Serializes a {@code List} containing objects that extend {@code JSONSerializable}, and outputs them using the specified
     * output stream.
     *
     * @param list    The {@code List} containing the objects to serialize.
     * @param ostream The output stream to send the output to.
     * @param indent  Whether or not to indent the output.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> void writeJSONArray(List<T> list, OutputStream ostream, boolean indent) throws IOException {
        try {
            if (indent) {
                enableJsonIndentation(true);
                mapper.writeValue(ostream, list);
                enableJsonIndentation(false);
            } else {
                mapper.writeValue(ostream, list);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            throw e;
        }
    }

    public static <T extends JSONSerializable> void writeJSONArray(Set<T> set, Writer writer) throws IOException {
        writeJSONArray(set, writer, false);
    }

    /**
     * Serializes a {@code Set} containing objects that extend {@code JSONSerializable}, and outputs them using the specified
     * output stream.
     *
     * @param set    The {@code Set} containing the objects to serialize.
     * @param writer The output stream to send the output to.
     * @param indent Whether or not to indent the output.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> void writeJSONArray(Set<T> set, Writer writer, boolean indent) throws IOException {
        try {
            if (indent) {
                enableJsonIndentation(true);
                mapper.writeValue(writer, set);
                enableJsonIndentation(false);
            } else {
                mapper.writeValue(writer, set);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            throw e;
        }
    }

    /**
     * Serializes a {@code Set} containing objects that extend {@code JSONSerializable}, and outputs them using the specified
     * output stream.
     *
     * @param set     The {@code Set} containing the objects to serialize.
     * @param ostream The output stream to send the output to.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> void writeJSONArray(Set<T> set, OutputStream ostream) throws IOException {
        writeJSONArray(set, ostream, false);
    }

    public static <T extends JSONSerializable> void writeJSONArray(Set<T> set, OutputStream ostream, boolean indent) throws IOException {
        try {
            if (indent) {
                enableJsonIndentation(true);
                mapper.writeValue(ostream, set);
                enableJsonIndentation(false);
            } else {
                mapper.writeValue(ostream, set);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            throw e;
        }
    }

    /**
     * Serializes an array containing objects that extend {@code JSONSerializable}, and outputs them using the specified
     * output stream.
     *
     * @param writer The output stream to send the output to.
     * @param values The objects to serialize.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> void writeJSONArray(Writer writer, T... values) throws IOException {
        writeJSONArray(writer, false, values);
    }

    /**
     * Serializes an array containing objects that extend {@code JSONSerializable}, and outputs them using the specified
     * output stream.
     *
     * @param writer The output stream to send the output to.
     * @param indent Whether or not to indent the output.
     * @param values The objects to serialize.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> void writeJSONArray(Writer writer, boolean indent, T... values) throws IOException {
        try {
            if (indent) {
                enableJsonIndentation(true);
                mapper.writeValue(writer, values);
                enableJsonIndentation(false);
            } else {
                mapper.writeValue(writer, values);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            throw e;
        }
    }

    /**
     * Serializes an array containing objects that extend {@code JSONSerializable}, and outputs them using the specified
     * output stream.
     *
     * @param ostream The output stream to send the output to.
     * @param values  The objects to serialize.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> void writeJSONArray(OutputStream ostream, T... values) throws IOException {
        writeJSONArray(ostream, false, values);
    }

    /**
     * Serializes an array containing objects that extend {@code JSONSerializable}, and outputs them using the specified
     * output stream.
     *
     * @param ostream The output stream to send the output to.
     * @param indent  Whether or not to indent the output.
     * @param values  The objects to serialize.
     *
     * @throws java.io.IOException
     */
    public static <T extends JSONSerializable> void writeJSONArray(OutputStream ostream, boolean indent, T... values) throws IOException {
        try {
            if (indent) {
                enableJsonIndentation(true);
                mapper.writeValue(ostream, values);
                enableJsonIndentation(false);
            } else {
                mapper.writeValue(ostream, values);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            throw e;
        }
    }

    /**
     * Reads a JSON-encoded string, creates, and returns the object it represents.
     *
     * @param json The JSON string to parse.
     *
     * @return The object represented by the JSON string.
     *
     * @throws java.io.IOException
     */
    public <T extends JSONSerializable> T readJSON(String json) throws IOException {
        return (T) mapper.readValue(json, this.getClass());
    }

    /**
     * Reads a JSON-encoded string, creates, and returns the object it represents.
     *
     * @param istream The input stream pointing to a JSON object to parse.
     *
     * @return The object represented by the JSON string.
     *
     * @throws java.io.IOException
     */
    public <T extends JSONSerializable> T readJSON(InputStream istream) throws IOException {
        return (T) mapper.readValue(istream, this.getClass());
    }

    /**
     * Writes this object to the specified {@code Writer}.
     *
     * @param writer The destination {@code Writer}.
     *
     * @throws java.io.IOException
     */
    public void writeJSON(Writer writer) throws IOException {
        writeJSON(writer, false);
    }

    /**
     * Writes this object to the specified {@code Writer}.
     *
     * @param writer The destination {@code Writer}.
     * @param indent Whether or not to indent the output.
     *
     * @throws java.io.IOException
     */
    public void writeJSON(Writer writer, boolean indent) throws IOException {
        try {
            if (indent) {
                enableJsonIndentation(true);
                mapper.writeValue(writer, this);
                enableJsonIndentation(false);
            } else {
                mapper.writeValue(writer, this);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            throw e;
        }
    }

    /**
     * Writes this object to the specified {@code OutputStream}.
     *
     * @param ostream The {@code OutputStream} to write to.
     *
     * @throws java.io.IOException
     */
    public void writeJSON(OutputStream ostream) throws IOException {
        writeJSON(ostream, false);
    }

    /**
     * Writes this object to the specified {@code OutputStream}.
     *
     * @param ostream The {@code OutputStream} to write to.
     * @param indent  Whether or not to indent the output.
     *
     * @throws java.io.IOException
     */
    public void writeJSON(OutputStream ostream, boolean indent) throws IOException {
        try {
            if (indent) {
                enableJsonIndentation(true);
                mapper.writeValue(ostream, this);
                enableJsonIndentation(false);
            } else {
                mapper.writeValue(ostream, this);
            }
        } catch (IOException e) {
            enableJsonIndentation(false);
            throw e;
        }
    }

    /**
     * Writes this object to JSON and returns it as a {@code String}.
     *
     * @return The JSON string representation of this object.
     */
    public String writeJSON() {
        return writeJSON(false);
    }

    /**
     * Writes this object to JSON and returns it as a {@code String}.
     *
     * @param indent Whether or not to indent the output.
     *
     * @return The JSON string representation of this object.
     */
    public String writeJSON(boolean indent) {
        try {
            if (indent) {
                enableJsonIndentation(true);
                String out = mapper.writeValueAsString(this);
                enableJsonIndentation(false);
                return out;
            } else {
                return mapper.writeValueAsString(this);
            }
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Simply calls {@link JSONSerializable#writeJSON()} on this object.
     *
     * @return The JSON string representation of this object.
     */
    @Override
    public String toString() {
        return writeJSON();
    }

    /**
     * Simply calls {@link JSONSerializable#writeJSON(boolean)} on this object.
     *
     * @param indent Whether or not to indent the output.
     *
     * @return The JSON string representation of this object.
     */
    public String toString(boolean indent) {
        return writeJSON(indent);
    }

    private static void enableJsonIndentation(boolean indent) {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, indent);
    }
}
