package com.jaysan1292.groupproject.service.security;

import com.jaysan1292.groupproject.Global;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/** @author Jason Recillo */
public class EncryptionUtilsTest {
    String userId, password;
    int authenticationLevel;
    DateTime generationDate;
    String token;

    @Before
    public void setUp() throws Exception {
        userId = RandomStringUtils.randomNumeric(6);
        password = RandomStringUtils.randomAlphanumeric(24);
        generationDate = new DateTime(2012, 10, 26, 23, 11, 35, 357);
        Global.log.info("User ID: " + userId);
        Global.log.info("Password: " + password);
        Global.log.info("Date: " + generationDate);
        authenticationLevel = AuthenticationLevel.ADMINISTRATOR;
        token = EncryptionUtils.generateAuthToken(userId, password.toCharArray(), authenticationLevel, generationDate);
        Global.log.debug(token);
        Global.log.debug("");
    }

    @Test
    public void testDecryptAuthToken() throws Exception {
        String decrypted = EncryptionUtils.decryptAuthToken(token, password.toCharArray());
        Global.log.debug(decrypted);
        String[] tokenParts = StringUtils.split(decrypted, '|');

        assertArrayEquals(new String[]{tokenParts[0], userId, String.valueOf(authenticationLevel), String.valueOf(generationDate.getMillis())}, tokenParts);
    }
}
