package com.jaysan1292.groupproject.service.security;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

/**
 * @author Jason Recillo
 */

/** A helper class meant to simplify encryption. */
public final class EncryptionUtils {

    private static final ConfigurablePasswordEncryptor encryptor;

    static {
        encryptor = new ConfigurablePasswordEncryptor();
        encryptor.setStringOutputType("hexadecimal");
        encryptor.setAlgorithm("MD5");
        encryptor.setPlainDigest(true);
    }

    private EncryptionUtils() {}

    public static String encryptPassword(String password) {
        return encryptor.encryptPassword(password);
    }

    public static boolean checkPassword(String plainPassword, String encryptedPassword) {
        return encryptor.checkPassword(plainPassword, encryptedPassword);
    }
}
