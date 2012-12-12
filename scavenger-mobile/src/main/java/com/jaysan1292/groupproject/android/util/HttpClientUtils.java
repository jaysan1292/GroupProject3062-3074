package com.jaysan1292.groupproject.android.util;

import com.google.common.net.HttpHeaders;
import com.sun.jersey.core.util.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 12/12/12
 * Time: 12:06 PM
 *
 * @author Jason Recillo
 */
public class HttpClientUtils {
    private HttpClientUtils() {}

    public static String getStringEntity(HttpResponse response) {
        try {
            InputStream inputStream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                StringBuilder sb = new StringBuilder();
                do {
                    sb.append(reader.readLine());
                } while (reader.read() != -1);

                return sb.toString();
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Header createAuthHeader(String username, String password) {
        String authHeader = String.format("Basic %s", new String(Base64.encode(username + ':' + password)));
        return new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
    }

    public static HttpGet createGetRequest(URI uri, Header authHeader) {
        HttpGet request = new HttpGet(uri);
        request.addHeader(authHeader);
        return request;
    }

    public static HttpPut createPutRequest(URI uri, Header authHeader) {
        HttpPut request = new HttpPut(uri);
        request.addHeader(authHeader);
        return request;
    }

    public static HttpPost createPostRequest(URI uri, Header authHeader) {
        HttpPost request = new HttpPost(uri);
        request.addHeader(authHeader);
        return request;
    }

    public static HttpDelete createDeleteRequest(URI uri, Header authHeader) {
        HttpDelete request = new HttpDelete(uri);
        request.addHeader(authHeader);
        return request;
    }
}
