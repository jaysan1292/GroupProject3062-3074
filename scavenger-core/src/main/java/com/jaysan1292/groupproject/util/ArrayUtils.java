package com.jaysan1292.groupproject.util;

import java.util.Arrays;

/** @author Jason Recillo */
public class ArrayUtils {
    private ArrayUtils() {}

    public static <T> T[] append(T[] array, T lastElement) {
        int n = array.length;
        array = Arrays.copyOf(array, n + 1);
        array[n] = lastElement;
        return array;
    }

    public static <T> T[] prepend(T[] array, T firstElement) {
        int n = array.length;
        array = Arrays.copyOf(array, n + 1);
        System.arraycopy(array, 0, array, 1, n);
        array[0] = firstElement;
        return array;
    }
}
