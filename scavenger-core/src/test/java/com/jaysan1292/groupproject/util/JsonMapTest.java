package com.jaysan1292.groupproject.util;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/** @author Jason Recillo */
public class JsonMapTest {
    private JsonMap map;

    @Before
    public void setUp() throws Exception {
        map = new JsonMap(true) {{
            put("string", "a string");
            put("booleanLiteralTrue", true);
            put("booleanStringTrue", "true");
            put("booleanIntTrue", 1);
            put("booleanLiteralFalse", false);
            put("booleanStringFalse", "false");
            put("booleanIntFalse", 0);
            put("byte", 0xC);
            put("short", 10);
            put("shortButNotReally", 289364873);
            put("long", 150934753845745859L);
            put("longString", "150934753845745859L");
            put("veryLong", "138917283239847286423");
            put("veryLong2", "138917283239847286423L");
            put("float", 32.3455465);
            put("floatString", "33.45456656532");
            put("floatNaN", Float.NaN);
            put("floatNaNString", "NaN");
            put("double", 46.234387434545653446);
            put("doubleString", "32.34276372343849");
            put("doubleNaN", Double.NaN);
            put("doubleNaNString", "NaN");
            put("byteArray", new byte[]{32, 64, 96, 24, 127});
            put("date", new DateTime(127000000000L));
            put("dateLong", 127000000000L);
        }};
    }

    @Test
    public void testStringConstructor() throws Exception {

    }

    @Test
    public void testGetString() throws Exception {
        assertEquals("a string", map.getString("string"));
    }

    @Test
    public void testGetBoolean() throws Exception {
        assertEquals(true, map.getBoolean("booleanLiteralTrue"));
        assertEquals(true, map.getBoolean("booleanStringTrue"));
        assertEquals(true, map.getBoolean("booleanIntTrue"));
        assertEquals(false, map.getBoolean("booleanLiteralFalse"));
        assertEquals(false, map.getBoolean("booleanStringFalse"));
        assertEquals(false, map.getBoolean("booleanIntFalse"));
    }

    @Test
    public void testGetByte() throws Exception {
        assertEquals(0xC, map.getByte("byte"));
    }

    @Test
    public void testGetShort() throws Exception {
        assertEquals(10, map.getShort("short"));
        assertNotSame(289364873, map.getShort("shortButNotReally"));
    }

    @Test
    public void testGetInt() throws Exception {
        assertEquals(0xC, map.getInt("byte"));
        assertEquals(10, map.getInt("short"));
        assertEquals(289364873, map.getInt("shortButNotReally"));
    }

    @Test
    public void testGetLong() throws Exception {
        assertEquals(150934753845745859L, map.getLong("long"));
        assertEquals(150934753845745859L, map.getLong("longString"));
        assertEquals(0, map.getLong("veryLong"));
        assertEquals(0, map.getLong("veryLong2"));
    }

    @Test
    public void testGetFloat() throws Exception {
        assertTrue(new Float(32.3455465f).equals(map.getFloat("float")));
        assertTrue(new Float(33.45456656532f).equals(map.getFloat("floatString")));
        assertTrue(new Float(Float.NaN).equals(map.getFloat("floatNaN")));
        assertTrue(new Float(Float.NaN).equals(map.getFloat("floatNaNString")));
    }

    @Test
    public void testGetDouble() throws Exception {
        assertTrue(new Double(46.234387434545653446).equals(map.getDouble("double")));
        assertTrue(new Double(32.34276372343849).equals(map.getDouble("doubleString")));
        assertTrue(new Double(Double.NaN).equals(map.getDouble("doubleNaN")));
        assertTrue(new Double(Double.NaN).equals(map.getDouble("doubleNaNString")));
    }

    @Test
    public void testGetBytes() throws Exception {
        assertArrayEquals(new byte[]{32, 64, 96, 24, 127}, map.getBytes("byteArray"));
    }

    @Test
    public void testGetDate() throws Exception {
        assertEquals(new DateTime(127000000000L), map.getDate("date"));
        assertEquals(new DateTime(127000000000L), map.getDate("dateLong"));
    }
}
