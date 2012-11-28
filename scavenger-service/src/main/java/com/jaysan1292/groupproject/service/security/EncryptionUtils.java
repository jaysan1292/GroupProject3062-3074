package com.jaysan1292.groupproject.service.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * @author Jason Recillo
 */

/** A helper class meant to simplify encryption. */
public final class EncryptionUtils {

    private static final ConfigurablePasswordEncryptor encryptor;
    private static final SimpleStringPBEConfig stringEncryptorConfig;
    private static StandardPBEStringEncryptor stringEncryptor;

    static {
        encryptor = new ConfigurablePasswordEncryptor();
        encryptor.setStringOutputType("hexadecimal");
        encryptor.setAlgorithm("SHA-256");
        encryptor.setPlainDigest(true);

        stringEncryptorConfig = new SimpleStringPBEConfig();
        stringEncryptorConfig.setStringOutputType("base64");
    }

    private EncryptionUtils() {}

    public static String encryptPassword(String password) {
        return encryptor.encryptPassword(password);
    }

    protected static String generateAuthToken(String userId, char[] password, int authenticationLevel, DateTime generationDate) {
        initializeStringEncryptor(password);

        String key = UUID.randomUUID().toString().toUpperCase() + '|' + userId + '|' + authenticationLevel + '|' + generationDate.getMillis();
        return stringEncryptor.encrypt(key);
    }

    protected static String decryptAuthToken(String authToken, char[] password) {
        initializeStringEncryptor(password);

        return stringEncryptor.decrypt(authToken);
    }

    private static void initializeStringEncryptor(char[] password) {
        stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setConfig(stringEncryptorConfig);
        stringEncryptor.setPasswordCharArray(password);
    }
}
